package com.ning.schedule;

import com.ning.repo.OnlineRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChatRoomSchedule {

    private final OnlineRepo onlineRepo;

    @Autowired
    public ChatRoomSchedule(OnlineRepo onlineRepo) {
        this.onlineRepo = onlineRepo;
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void sendPing() {
        long startTime = System.currentTimeMillis();
        onlineRepo.schedule();
        log.info("ChatRoomSchedule # sendPing end !!! Duration={} ms !!!", System.currentTimeMillis() - startTime);
    }

    @Scheduled(fixedRate = 1000 * 60 * 5)
    public void printClients() {
        int count = onlineRepo.findOnlineClientsCount();
        log.info("ChatRoomSchedule # printClients online socket client count={}", count);
        onlineRepo.printClients();
    }

}
