package dev.gustavoteixeira.api.votingsession.controller;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(path = "/agenda")
public class AgendaController {

    final Logger logger = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<Void> createAgenda(@RequestBody final AgendaRequestDTO agendaRequest) {
        logger.info("AgendaController.createAgenda - Start - Agenda: {}", agendaRequest);

        var agenda = agendaService.createAgenda(agendaRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agenda.getId())
                .toUri();

        logger.debug("AgendaController.createAgenda - End - Agenda identifier: {}", agenda.getId());
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{agendaId}")
    public ResponseEntity<AgendaResponseDTO> getAgenda(@PathVariable String agendaId) {
        logger.info("AgendaController.getAgenda - Start - Agenda identifier: {}", agendaId);

        AgendaResponseDTO agenda = agendaService.getAgenda(agendaId);

        return ResponseEntity.ok(agenda);
    }

    @PatchMapping("/{agendaId}/start")
    public ResponseEntity<Void> startAgenda(@PathVariable String agendaId) {
        logger.info("AgendaController.startAgenda - Start - Agenda identifier: {}", agendaId);

        agendaService.startAgenda(agendaId);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{agendaId}/vote")
    public ResponseEntity<VoteResponseDTO> voteAgenda(@PathVariable String agendaId, @RequestBody final VoteRequestDTO voteRequest) {
        logger.info("AgendaController.voteAgenda - Start - Agenda identifier: {}", agendaId);

        VoteResponseDTO vote = agendaService.voteAgenda(agendaId, voteRequest);

        return ResponseEntity.ok(vote);
    }


}
