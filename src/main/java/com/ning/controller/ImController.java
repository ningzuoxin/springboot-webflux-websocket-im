package com.ning.controller;

import com.ning.common.RedisKey;
import com.ning.model.JsonResponse;
import com.ning.repo.ClientInfo;
import com.ning.repo.OnlineRepo;
import com.ning.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/im/")
public class ImController {

    @Autowired
    RedisTemplate<String, String> redisTemplate;
    @Autowired
    OnlineRepo onlineRepo;

    @GetMapping(value = {"/", "index"})
    public String index(ServerWebExchange exchange, final Model model) {
        String ip = "";

        ServerHttpRequest request = exchange.getRequest();
        ip = this.getIpAddress(request);

        String key = RedisKey.IP + ip;
        String id = redisTemplate.opsForValue().get(key);
        if (StringUtils.isEmpty(id)) {
            id = System.currentTimeMillis() + "";
            redisTemplate.opsForValue().set(key, id);
        }

        String nonce = System.currentTimeMillis() + "";
        String authorization = this.digestAuthorization(id, nonce);

        model.addAttribute("ip", ip);
        model.addAttribute("id", id);
        model.addAttribute("nonce", nonce);
        model.addAttribute("authorization", authorization);
        log.info("ImController # index ip={},id={},nonce={},authorization={}", ip, id, nonce, authorization);
        return "im_index";
    }

    @ResponseBody
    @GetMapping(value = "/online")
    public Mono<JsonResponse> getOnlineClients() {
        List<ClientInfo> clients = onlineRepo.findTerminalClients("0");
        return Mono.just(JsonResponse.success().addInfo(clients));
    }

    private String digestAuthorization(String id, String nonce) {
        String[] arr = new String[0];
        try {
            id = Utils.URLDecoderParam(id);
            arr = new String[]{URLEncoder.encode(id, "UTF-8"), nonce};
        } catch (UnsupportedEncodingException e) {
            log.error("ImController # digestAuthorization UnsupportedEncodingException={}", e);
        }

        Arrays.sort(arr);
        StringBuilder input = new StringBuilder();
        for (String s : arr) {
            input.append(s);
        }

        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            log.error("ImController # digestAuthorization NoSuchAlgorithmException={}", e);
        }
        byte[] hash = messageDigest.digest(input.toString().getBytes(StandardCharsets.UTF_8));
        String encodeStr = Hex.encodeHexString(hash);

        return encodeStr;
    }

    /**
     * 获取ip
     *
     * @param request
     * @return
     */
    private String getIpAddress(ServerHttpRequest request) {
        HttpHeaders headers = request.getHeaders();
        String ip = headers.getFirst("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = headers.getFirst("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddress().getAddress().getHostAddress();
        }
        return ip;
    }

}
