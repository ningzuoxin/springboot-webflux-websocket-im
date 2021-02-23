package com.ning.schedule;

import com.ning.repo.OnlineRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ImJob {

    private final OnlineRepo onlineRepo;

    @Autowired
    public ImJob(OnlineRepo onlineRepo) {
        this.onlineRepo = onlineRepo;
    }

    @Scheduled(fixedRate = 1000 * 100)
    public void sendPing() {
        long startTime = System.currentTimeMillis();
        onlineRepo.schedule();
        log.info("ImJob # sendPing job end !!! Duration={} ms !!!", System.currentTimeMillis() - startTime);
    }

    @Scheduled(fixedRate = 1000 * 10)
    public void printClients() {
        int count = onlineRepo.findOnlineClientsCount();
        log.info("ImJob # printClients online socket client count={}", count);
        onlineRepo.printClients();
    }

}
