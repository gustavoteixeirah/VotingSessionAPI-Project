package dev.gustavoteixeira.api.votingsession.scheduler;

import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import dev.gustavoteixeira.api.votingsession.service.MessagingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.springframework.util.CollectionUtils.isEmpty;

@Component
@EnableAsync
@EnableScheduling
public class AgendaResultPublishingSchedule {

    final Logger logger = LoggerFactory.getLogger(AgendaResultPublishingSchedule.class);

    @Autowired
    private AgendaService agendaService;

    @Autowired
    private MessagingService messagingService;

    @Async
    @Scheduled(fixedDelay = 60000)
    public void publishAgendaResult() {
        logger.info("AgendaResultPublishingSchedule.publishAgendaResult - Start");
        List<Agenda> agendas = agendaService.getAllUnpublishedAgendas();

        if (!isEmpty(agendas)) {
            logger.debug("AgendaResultPublishingSchedule.publishAgendaResult - {} unpublished agendas", agendas.size());
            agendas.forEach(agenda -> {
                var agendaResponse = agendaService.getAgendaResponseDTOWithCountedVotes(agenda);
                logger.debug("AgendaResultPublishingSchedule.publishAgendaResult - Publishing: {}", agendaResponse);
                messagingService.publishMessageOnQueue(agendaResponse);
            });
        }

        logger.info("AgendaResultPublishingSchedule.publishAgendaResult - End");
    }

}
