package com.ning;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class ChatRoomApplication {

    public static void main(String[] args) {
        SpringApplication.run(ChatRoomApplication.class, args);
        log.info("chat room application start success !!!");
    }

}
