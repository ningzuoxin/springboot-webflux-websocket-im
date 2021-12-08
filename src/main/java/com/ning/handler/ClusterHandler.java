package com.ning.handler;

import com.ning.model.AuthModel;
import com.ning.model.JsonResponse;
import com.ning.model.ServerNodeInfo;
import com.ning.repo.OnlineRepo;
import com.ning.utils.Utils;
import com.sun.istack.internal.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

@Slf4j
@Component
public class ClusterHandler {

    @Value("${websocket.url}")
    private String websocketUrl;

    private final OnlineRepo onlineRepo;

    @Autowired
    public ClusterHandler(OnlineRepo onlineRepo) {
        this.onlineRepo = onlineRepo;
    }

    public @NotNull
    Mono<ServerResponse> access(ServerRequest request) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(auth(request));
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
            info.addr = "ws://" + findRestWebSocketServiceAddress() + "/c";
            info.token = token;
            return JsonResponse.success().addInfo(info);
        }

        log.error("ClusterHandler Authorization failed, param Authorization={},deviceId={},nonce={}", Authorization, deviceIdStr, nonceStr);
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
                log.error("ClusterHandler # authByDeviceIdAndNonce UnsupportedEncodingException={}", e.getMessage());
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
                log.error("ClusterHandler # authByDeviceIdAndNonce NoSuchAlgorithmException={}", e.getMessage());
                e.printStackTrace();
            }
        }
        return authModel;
    }

    private String findRestWebSocketServiceAddress() {
        String address = websocketUrl;
        if (StringUtils.isEmpty(address)) {
            return "";
        }
        return address;
    }
}
