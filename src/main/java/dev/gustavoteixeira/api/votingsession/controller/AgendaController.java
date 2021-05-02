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

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/agenda")
public class AgendaController {

    final Logger logger = LoggerFactory.getLogger(AgendaController.class);

    @Autowired
    private AgendaService agendaService;

    /**
     * Esse método é responsável por validar a requisição de criação de uma nova agenda e,
     * se estiver tudo certo, repassar essa requisição para a camada de serviço executá-la.
     * Como retorno, é adicionado no header da resposta da requisição a localização
     * da agenda que foi criada.
     */
    @PostMapping
    public ResponseEntity<Void> createAgenda(@Valid @RequestBody final AgendaRequestDTO agendaRequest) {
        logger.info("AgendaController.createAgenda - Start - Agenda name: {}", agendaRequest.getName());

        var agenda = agendaService.createAgenda(agendaRequest);

        var location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(agenda.getId())
                .toUri();

        logger.debug("AgendaController.createAgenda - End - Agenda name: {}, Agenda identifier: {}", agenda.getName(), agenda.getId());
        return ResponseEntity.created(location).build();
    }

    /**
     * Esse método é responsável por receber a requisição de obtenção de uma agenda  e
     * repassar essa requisição para a camada de serviço executá-la.
     * O retorno é uma objeto de resposta da agenda: AgendaResponseDTO
     */
    @GetMapping("/{agendaId}")
    public ResponseEntity<AgendaResponseDTO> getAgenda(@PathVariable String agendaId) {
        logger.info("AgendaController.getAgenda - Start - Agenda identifier: {}", agendaId);

        var agenda = agendaService.getAgenda(agendaId);

        logger.debug("AgendaController.getAgenda - End - Agenda identifier: {}", agendaId);
        return ResponseEntity.ok(agenda);
    }

    /**
     * Esse método é responsável por receber a requisição de início de uma agenda  e
     * repassar essa requisição para a camada de serviço executá-la.
     * O retorno, em caso de sucesso, é o status OK.
     */
    @PatchMapping("/{agendaId}/start")
    public ResponseEntity<Void> startAgenda(@PathVariable String agendaId) {
        logger.info("AgendaController.startAgenda - Start - Agenda identifier: {}", agendaId);

        agendaService.startAgenda(agendaId);

        logger.debug("AgendaController.startAgenda - End - Agenda identifier: {}", agendaId);
        return ResponseEntity.ok().build();
    }

    /**
     * Esse método é responsável por receber a requisição de voto em uma agenda  e
     * repassar essa requisição para a camada de serviço executá-la.
     * Para isso, ela deve receber a agenda a qual o voto deverá ser contabilizado,
     * e também o objeto do voto, que contém o identificador do associado e a
     * sua escolha.
     * O retorno, em caso de sucesso, é o status OK.
     */
    @PostMapping("/{agendaId}/vote")
    public ResponseEntity<Void> voteAgenda(@PathVariable String agendaId, @RequestBody final VoteRequestDTO voteRequest) {
        logger.info("AgendaController.voteAgenda - Start - Agenda identifier: {}, Associate: {}", agendaId, voteRequest.getAssociate());

        agendaService.voteAgenda(agendaId, voteRequest);

        logger.debug("AgendaController.voteAgenda - End - Agenda identifier: {}, Associate: {}", agendaId, voteRequest.getAssociate());
        return ResponseEntity.ok().build();
    }

}
