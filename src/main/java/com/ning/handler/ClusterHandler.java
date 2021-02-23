package com.ning.handler;

import com.alibaba.fastjson.JSON;
import com.ning.model.AuthModel;
import com.ning.model.JsonResponse;
import com.ning.model.ServerNodeInfo;
import com.ning.repo.OnlineRepo;
import com.ning.repo.SubscribeRepo;
import com.ning.utils.Utils;
import com.sun.istack.internal.NotNull;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

@Component
public class ClusterHandler {

    private static final Logger logger = LoggerFactory.getLogger(ClusterHandler.class);

    RedisTemplate<String, String> redisTemplate;
    OnlineRepo onlineRepo;
    SubscribeRepo subscribeRepo;

    private final static String IM_URL = "123.207.93.191:8081";

    @Autowired
    ClusterHandler(OnlineRepo onlineRepo, SubscribeRepo subscribeRepo, RedisTemplate<String, String> redisTemplate) {
        this.onlineRepo = onlineRepo;
        this.redisTemplate = redisTemplate;
        this.subscribeRepo = subscribeRepo;
    }

    public @NotNull
    Mono<ServerResponse> access(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(auth(request));
    }

    public @NotNull
    Mono<ServerResponse> subscribe(ServerRequest request) {
        return request.formData().flatMap(map -> {
            String business = map.getFirst("business");
            String deviceId = map.getFirst("device");
            subscribeRepo.subscribe(business, deviceId);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("subscribe");
        });
    }

    public @NotNull
    Mono<ServerResponse> unSubscribe(ServerRequest request) {
        return request.formData().flatMap(map -> {
            String business = map.getFirst("business");
            String deviceId = map.getFirst("device");
            subscribeRepo.unsubscribe(business, deviceId);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("unSubscribe");
        });
    }

    public @NotNull
    Mono<ServerResponse> createSubscribe(ServerRequest request) {
        return request.formData().flatMap(map -> {
            String business = map.getFirst("business");
            String ttl = map.getFirst("ttl");
            subscribeRepo.createBusinessSubscribe(business, ttl);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("createSubscribe");
        });
    }

    public @NotNull
    Mono<ServerResponse> destroySubscribe(ServerRequest request) {
        return request.formData().flatMap(map -> {
            String business = map.getFirst("business");
            subscribeRepo.destroyBusinessSubscribe(business);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue("destroySubscribe");
        });
    }

    /**
     * 接口鉴权
     * 1.Authorization URLEncode后的deviceId拼接nonce做SHA256加密传输到服务端。
     * 2.获取deviceId和nonce服务端做SHA256加密后的Authorization做一次对比，相等则验证通过。
     *
     * @param request
     * @return
     */
    private JsonResponse auth(ServerRequest request) {
        String Authorization = request.headers().firstHeader("Authorization");
        logger.info("ClusterHandler # auth Authorization={}", Authorization);

        if (Authorization == null) {
            return JsonResponse.failure(HttpStatus.UNAUTHORIZED.value(), "No Authorization Info");
        }
        String[] auth = Authorization.split(" "); // IM c570b50e454f708b79da589dbcd2920b8fe5e4
        if (auth.length == 2) {
            if (auth[0].equals("IM")) {
                Authorization = auth[1]; // c570b50e454f708b79da589dbcd2920b8fe5e4
            } else {
                return JsonResponse.failure(HttpStatus.UNAUTHORIZED.value(), "Authorization failed by Auth Key");
            }
        } else {
            return JsonResponse.failure(HttpStatus.UNAUTHORIZED.value(), "Authorization failed by Auth length");
        }
        Optional<String> deviceId = request.queryParam("u");
        Optional<String> nonce = request.queryParam("n");

        String deviceIdStr = "";
        String nonceStr = "";
        if (deviceId.isPresent()) {
            deviceIdStr = deviceId.get();
        }
        if (nonce.isPresent()) {
            nonceStr = nonce.get();
        }

        AuthModel authModel = authByDeviceIdAndNonce(deviceIdStr, nonceStr);
        if (Authorization.equals(authModel.getAuthorization())) {
            String token = onlineRepo.login(authModel.getDeviceId(), authModel.getAuthorization());
            ServerNodeInfo info = new ServerNodeInfo();
            info.addr = "ws://" + FindRestWebSocketServiceAddress() + "/c";
            info.token = token;
            logger.info("ClusterHandler # auth success ServerNodeInfo={}", JSON.toJSONString(info));
            return JsonResponse.success().addInfo(info);
        }

        logger.error("ClusterHandler Authorization failed, param Authorization={},deviceId={},nonce={}", Authorization, deviceIdStr, nonceStr);
        return JsonResponse.failure(HttpStatus.UNAUTHORIZED.value(), "Authorization failed by IM Server");
    }

    /**
     * 根据设备号（deviceId）和随机串（nonce）计算Authorization
     *
     * @param deviceId
     * @param nonce
     * @return
     */
    private AuthModel authByDeviceIdAndNonce(String deviceId, String nonce) {
        logger.info("ClusterHandler # authByDeviceIdAndNonce deviceId={}", deviceId);
        AuthModel authModel = new AuthModel();
        if (deviceId.contains("%")) {
            authModel.setDeviceId(Utils.URLDecoderParam(deviceId));
        } else {
            authModel.setDeviceId(deviceId);
        }
        authModel.setAuthorization("");

        if (!StringUtils.isEmpty(deviceId) && !StringUtils.isEmpty(nonce)) {
            String[] arr = new String[0];
            try {
                arr = new String[]{URLEncoder.encode(authModel.getDeviceId(), "UTF-8"), nonce};
            } catch (UnsupportedEncodingException e) {
                logger.error("ClusterHandler # authByDeviceIdAndNonce UnsupportedEncodingException={}", e.getMessage());
            }

            Arrays.sort(arr);
            StringBuilder input = new StringBuilder();
            for (String s : arr) {
                input.append(s);
            }

            MessageDigest messageDigest = null;
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
                byte[] hash = messageDigest.digest(input.toString().getBytes(StandardCharsets.UTF_8));
                String encodeStr = Hex.encodeHexString(hash);
                authModel.setAuthorization(encodeStr);
                return authModel;
            } catch (NoSuchAlgorithmException e) {
                logger.error("ClusterHandler # authByDeviceIdAndNonce NoSuchAlgorithmException={}", e.getMessage());
                e.printStackTrace();
            }
        }
        return authModel;
    }

    private String FindRestWebSocketServiceAddress() {
        String addr = IM_URL;
        if (StringUtils.isEmpty(addr)) {
            return "app.foundersc.com/sloth/ws";
        }
        return addr;
    }
}
