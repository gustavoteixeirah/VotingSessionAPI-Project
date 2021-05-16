package dev.gustavoteixeira.api.votingsession.service.impl;

import dev.gustavoteixeira.api.votingsession.service.MessagingService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static dev.gustavoteixeira.api.votingsession.config.MessagingConfig.EXCHANGE;
import static dev.gustavoteixeira.api.votingsession.config.MessagingConfig.ROUTING_KEY;

@Service
public class MessagingServiceImpl implements MessagingService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void publishMessageOnQueue(Object obj) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, obj);
    }

}
