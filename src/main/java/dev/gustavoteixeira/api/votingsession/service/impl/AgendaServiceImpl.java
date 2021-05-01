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
    public Agenda createAgenda(AgendaRequestDTO agendaRequest) {

        Agenda agenda = Agenda.builder()
                .name(agendaRequest.getName())
                .duration(agendaRequest.getDuration())
                .startTime(LocalDateTime.now())
                .build();

        try {
            agenda = agendaRepository.insert(agenda);
        } catch (DuplicateKeyException e) {
            throw new AgendaAlreadyExistsException();
        }

        return agenda;
    }

}
