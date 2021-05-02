package dev.gustavoteixeira.api.votingsession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VotingSessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(VotingSessionApplication.class, args);
    }

}
