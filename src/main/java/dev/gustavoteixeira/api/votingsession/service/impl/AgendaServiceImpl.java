package dev.gustavoteixeira.api.votingsession.service.impl;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.entity.Vote;
import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaClosedException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaNotFoundException;
import dev.gustavoteixeira.api.votingsession.exception.VoteAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.api.votingsession.repository.VoteRepository;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static dev.gustavoteixeira.api.votingsession.converter.AgendaConverter.getAgendaResponse;

@Service
public class AgendaServiceImpl implements AgendaService {

    final Logger logger = LoggerFactory.getLogger(AgendaServiceImpl.class);

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Agenda createAgenda(AgendaRequestDTO agendaRequest) {

        //TODO Use object mapper
        Agenda agenda = Agenda.builder()
                .name(agendaRequest.getName())
                .duration(agendaRequest.getDuration())
                .build();

        return registerAgenda(agenda);
    }

    @Override
    public AgendaResponseDTO getAgenda(String agendaId) {
        Agenda agenda = verifyIfAgendaExist(agendaId);


        AgendaResponseDTO agendaResponse = getAgendaResponse(agenda);

        //TODO metodo para contabilizar os votos

        return agendaResponse;
    }

    @Override
    public void startAgenda(String agendaId) {
        Agenda agenda = verifyIfAgendaExist(agendaId);
        agenda.setStartTime(LocalDateTime.now());
        agendaRepository.save(agenda);
    }

    @Override
    public Vote voteAgenda(String agendaId, VoteRequestDTO voteRequest) {
        Agenda agenda = verifyIfAgendaExist(agendaId);

        verifyIfAgendaIsOpen(agenda);

        //TODO verify if associate is able to vote by checking the cpf with the validator provided on the challenge description [Tarefa bonus 1]
        // suggested method name "validateAssociate(String associateCPF)"

        //TODO Use object mapper
        Vote vote = Vote.builder()
                .agendaId(agendaId)
                .associate(voteRequest.getAssociate())
                .choice(voteRequest.getChoice())
                .build();

        return registerVote(vote);
    }

    private void verifyIfAgendaIsOpen(Agenda agenda) {
        if (!agendaIsOpen(agenda)) {
            throw new AgendaClosedException();
        }
    }

    public static boolean agendaIsOpen(Agenda agenda) {
        return agenda.getStartTime() != null && (agenda.getStartTime().plusMinutes(agenda.getDuration()).isAfter(LocalDateTime.now()));
    }

    private Agenda registerAgenda(Agenda agenda) {
        try {
            agenda = agendaRepository.insert(agenda);
        } catch (DuplicateKeyException e) {
            logger.error("AgendaServiceImpl.registerAgenda - Error - Agenda already exists " +
                    "- Agenda name: {}", agenda.getName());
            throw new AgendaAlreadyExistsException();
        }
        return agenda;
    }

    private Vote registerVote(Vote vote) {
        try {
            vote = voteRepository.insert(vote);
        } catch (DuplicateKeyException e) {
            logger.error("AgendaServiceImpl.registerVote - Error - Vote already exists " +
                    "- Associate: {}, Choice: {}", vote.getAssociate(), vote.getChoice());
            throw new VoteAlreadyExistsException();
        }
        return vote;
    }

    private Agenda verifyIfAgendaExist(String agendaId) {
        Optional<Agenda> agendaOptional = agendaRepository.findById(agendaId);
        return agendaOptional.orElseThrow(AgendaNotFoundException::new);
    }

}
