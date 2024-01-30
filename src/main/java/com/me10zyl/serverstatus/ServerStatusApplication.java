package com.me10zyl.serverstatus;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerStatusApplication {

    @Value("${server.port}")
    private Integer serverPort;

    @PostConstruct
    public void init(){
        System.out.println("run at port " + serverPort);
    }

    public static void main(String[] args) {
        SpringApplication.run(ServerStatusApplication.class, args);
    }

}
