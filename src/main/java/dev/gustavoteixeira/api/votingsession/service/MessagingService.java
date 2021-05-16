package dev.gustavoteixeira.api.votingsession.service;


public interface MessagingService {

    void publishMessageOnQueue(Object obj);

}
