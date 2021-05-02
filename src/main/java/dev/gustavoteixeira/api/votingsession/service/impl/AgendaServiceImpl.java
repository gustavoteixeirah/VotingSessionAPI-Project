package dev.gustavoteixeira.api.votingsession.service.impl;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.entity.Vote;
import dev.gustavoteixeira.api.votingsession.exception.*;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.api.votingsession.repository.VoteRepository;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static dev.gustavoteixeira.api.votingsession.converter.AgendaConverter.getAgendaResponse;
import static dev.gustavoteixeira.api.votingsession.converter.VoteConverter.getVoteResponse;

@Service
public class AgendaServiceImpl implements AgendaService {

    public static final String NEGATIVE_VOTE = "NÂO";
    public static final String POSITIVE_VOTE = "SIM";
    final Logger logger = LoggerFactory.getLogger(AgendaServiceImpl.class);

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Override
    public Agenda createAgenda(AgendaRequestDTO agendaRequest) {

        var agenda = Agenda.builder()
                .name(agendaRequest.getName())
                .duration(agendaRequest.getDuration())
                .build();

        return registerAgenda(agenda);
    }

    @Override
    public AgendaResponseDTO getAgenda(String agendaId) {
        var agenda = verifyIfAgendaExist(agendaId);

        AgendaResponseDTO agendaResponse = getAgendaResponse(agenda);

        countVotes(agendaResponse);

        return agendaResponse;
    }

    private void countVotes(AgendaResponseDTO agendaResponse) {
        List<Vote> votes = voteRepository.findAllByAgendaId(agendaResponse.getId());
        agendaResponse.setNegativeVotes(votes.stream()
                .filter(vote -> vote.getChoice().equals(NEGATIVE_VOTE)).count());
        agendaResponse.setPositiveVotes(votes.stream()
                .filter(vote -> vote.getChoice().equals(POSITIVE_VOTE)).count());
    }

    @Override
    public void startAgenda(String agendaId) {
        var agenda = verifyIfAgendaExist(agendaId);
        verifyIfAgendaIsAlreadyOpen(agenda);
        verifyIfAgendaIsClose(agenda);
        agenda.setStartTime(LocalDateTime.now());
        agendaRepository.save(agenda);
    }

    private void verifyIfAgendaIsClose(Agenda agenda) {
        if (!agendaIsOpen(agenda) && agenda.getStartTime() != null) {
            throw new AgendaHasAlreadyBeenClosedException();
        }
    }

    private void verifyIfAgendaIsAlreadyOpen(Agenda agenda) {
        if (agendaIsOpen(agenda)) {
            throw new AgendaIsAlreadyOpenException();
        }
    }

    @Override
    public VoteResponseDTO voteAgenda(String agendaId, VoteRequestDTO voteRequest) {
        //TODO verify if associate is able to vote by checking the cpf with the validator provided on the challenge description [Tarefa bonus 1]

        var agenda = verifyIfAgendaExist(agendaId);

        verifyIfAgendaIsOpen(agenda);
        verifyIfAssociateHaveNotVotedYet(agendaId, voteRequest);

        var vote = Vote.builder()
                .agendaId(agendaId)
                .associate(voteRequest.getAssociate())
                .choice(voteRequest.getChoice())
                .build();

        vote = registerVote(vote);

        return getVoteResponse(vote);
    }

    private void verifyIfAssociateHaveNotVotedYet(String agendaId, VoteRequestDTO voteRequest) {
        if (voteRepository.findByAssociateAndAgendaId(voteRequest.getAssociate(), agendaId) != null) {
            throw new VoteAlreadyExistsException();
        }
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
                    "- Associate: {}, Agenda identifier: {}", vote.getAssociate(), vote.getAgendaId());
            throw new VoteAlreadyExistsException();
        }
        return vote;
    }

    private Agenda verifyIfAgendaExist(String agendaId) {
        Optional<Agenda> agendaOptional = agendaRepository.findById(agendaId);
        return agendaOptional.orElseThrow(AgendaNotFoundException::new);
    }

}
