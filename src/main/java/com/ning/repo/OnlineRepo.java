package com.ning.repo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.ning.handler.IMSession;
import com.ning.utils.Md5Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class OnlineRepo {

    private final Map<String, ClientInfo> clients = new ConcurrentHashMap<>();

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public OnlineRepo(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public boolean auth(String deviceId, String token, IMSession ws) {
        if (deviceId != null && token != null && ws != null) {
            String savedToken = redisTemplate.opsForValue().get(deviceId);
            if (savedToken != null && savedToken.equals(token)) {
                ClientInfo c = new ClientInfo();
                c.deviceId = deviceId;
                c.session = ws;
                clients.put(deviceId, c);
                log.info("OnlineRepo # auth deviceId={} auth success,clientInfo={} !", deviceId, JSON.toJSONString(c));
                return true;
            }
        }
        return false;
    }

    public String login(String deviceId, String token) {
        String newToken = Md5Utils.stringMD5(token + System.currentTimeMillis());
        redisTemplate.opsForValue().set(deviceId, newToken, 10, TimeUnit.SECONDS);
        return newToken;
    }

    public boolean offline(String deviceId) {
        if (deviceId != null) {
            if (clients.containsKey(deviceId)) {
                clients.remove(deviceId);
                return true;
            }
        }
        return false;
    }

    public boolean bindPhoneNo(String deviceId, String phoneId) {
        log.info("OnlineRepo # bindPhoneNo deviceId={},phoneId={}", deviceId, phoneId);
        if (deviceId == null) return false;
        ClientInfo client = clients.get(deviceId);
        if (client == null) {
            return false;
        }
        client.deviceId = deviceId;
        client.phoneId = phoneId;
        return true;
    }

    public boolean bindCustomNo(String deviceId, String customId) {
        log.info("OnlineRepo # bindPhoneNo deviceId={},customId={}", deviceId, customId);
        ClientInfo client = clients.get(deviceId);
        if (client == null) {
            return false;
        }
        client.deviceId = deviceId;
        client.customId = customId;
        return true;
    }

    // 通过手机号找到匹配的设备id
    public List<ClientInfo> findDevicesByPhoneId(String phoneId) {
        if (StringUtils.isEmpty(phoneId)) {
            return Lists.newArrayList();
        }
        Collection<ClientInfo> cs = clients.values();
        return cs.stream().filter(c -> !StringUtils.isEmpty(c.phoneId) && phoneId.contains(c.phoneId)).collect(Collectors.toList());
    }

    // 通过客户号找到匹配的设备id
    public List<ClientInfo> findDevicesByCustomId(String customId) {
        if (StringUtils.isEmpty(customId)) {
            return Lists.newArrayList();
        }
        Collection<ClientInfo> cs = clients.values();
        log.info("OnlineRepo # findDevicesByCustomId customId={}", customId);
        return cs.stream().filter(c -> !StringUtils.isEmpty(c.customId) && customId.contains(c.customId)).collect(Collectors.toList());
    }

    /**
     * 根据终端类型查询对应的client 1:安卓 2:IOS 0:全部在线用户
     *
     * @param terminal
     * @return
     */
    public List<ClientInfo> findTerminalClients(String terminal) {
        if (terminal.equals("0")) {
            return new ArrayList<>(clients.values());
        }
        Collection<ClientInfo> cs = clients.values();
        return cs.stream().filter(c -> c.terminal.equals(terminal)).collect(Collectors.toList());
    }

    public ClientInfo getClientInfo(String deviceId) {
        return clients.get(deviceId);
    }

    public int findOnlineClientsCount() {
        return clients.size();
    }

    public void printClients() {
        clients.values().stream().forEach(client -> log.info("OnlineRepo # printClients online client={}", JSON.toJSONString(client)));
    }

    public void schedule() {
        Flux.fromStream(clients.values().stream()).subscribe(client -> client.session.sendPing());
    }

}
