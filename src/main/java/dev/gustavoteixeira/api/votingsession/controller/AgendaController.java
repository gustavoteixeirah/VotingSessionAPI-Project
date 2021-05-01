package dev.gustavoteixeira.api.votingsession.controller;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/agenda")
public class AgendaController {

    Logger logger = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private AgendaService agendaService;

    @PostMapping
    public ResponseEntity<?> createAgenda(@RequestBody final AgendaRequestDTO agendaRequest) {

        agendaService.createAgenda(agendaRequest);

        return ResponseEntity.ok().build();
    }

}
