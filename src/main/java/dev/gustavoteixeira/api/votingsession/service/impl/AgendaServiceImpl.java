package dev.gustavoteixeira.api.votingsession.service.impl;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AgendaServiceImpl implements AgendaService {

    @Autowired
    private AgendaRepository agendaRepository;

    @Override
    public void createAgenda(AgendaRequestDTO agendaRequest) {
        try {
            agendaRepository.insert(
                    Agenda.builder()
                            .name(agendaRequest.getName())
                            .duration(agendaRequest.getDuration())
                            .startTime(LocalDateTime.now())
                            .build());

        } catch (DuplicateKeyException e) {
            throw new AgendaAlreadyExistsException();
        }


    }

}
