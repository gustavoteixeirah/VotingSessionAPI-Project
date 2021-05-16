package dev.gustavoteixeira.api.votingsession.service.impl;

import dev.gustavoteixeira.api.votingsession.clients.AssociateValidatorClient;
import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
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
import java.util.stream.Collectors;

import static dev.gustavoteixeira.api.votingsession.converter.AgendaConverter.getAgendaResponse;

@Service
public class AgendaServiceImpl implements AgendaService {

    public static final String NEGATIVE_VOTE = "Não";
    public static final String POSITIVE_VOTE = "Sim";
    public static final int DEFAULT_DURATION = 1;

    final Logger logger = LoggerFactory.getLogger(AgendaServiceImpl.class);

    @Autowired
    private AgendaRepository agendaRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private AssociateValidatorClient associateValidatorClient;

    @Override
    public Agenda createAgenda(AgendaRequestDTO agendaRequest) {
        logger.info("AgendaServiceImpl.createAgenda - Start - Agenda name: {}", agendaRequest.getName());
        var agenda = Agenda.builder()
                .name(agendaRequest.getName())
                .duration(getDuration(agendaRequest))
                .build();

        return registerAgenda(agenda);
    }

    @Override
    public AgendaResponseDTO getAgenda(String agendaId) {
        logger.info("AgendaServiceImpl.getAgenda - Start - Agenda identifier: {}", agendaId);
        var agenda = verifyIfAgendaExist(agendaId);

        return getAgendaResponseDTOWithCountedVotes(agenda);
    }

    public AgendaResponseDTO getAgendaResponseDTOWithCountedVotes(Agenda agenda) {
        AgendaResponseDTO agendaResponse = getAgendaResponse(agenda);

        countVotes(agendaResponse);
        return agendaResponse;
    }

    @Override
    public void startAgenda(String agendaId) {
        logger.info("AgendaServiceImpl.startAgenda - Start - Agenda identifier: {}", agendaId);

        var agenda = verifyIfAgendaExist(agendaId);
        verifyIfAgendaIsAlreadyOpen(agenda);
        verifyIfAgendaHasAlreadyBeenClosed(agenda);

        agenda.setStartTime(LocalDateTime.now());
        agendaRepository.save(agenda);
    }

    @Override
    public void voteAgenda(String agendaId, VoteRequestDTO voteRequest) {
        logger.info("AgendaServiceImpl.voteAgenda - Start - Agenda identifier: {}, Associate: {}", agendaId, voteRequest.getAssociate());

        verifyIfAssociateIsAbleToVote(voteRequest);

        var agenda = verifyIfAgendaExist(agendaId);

        verifyIfAgendaIsOpen(agenda);
        verifyIfAssociateHaveNotVotedYet(agendaId, voteRequest);

        var vote = Vote.builder()
                .agendaId(agendaId)
                .associate(voteRequest.getAssociate())
                .choice(voteRequest.getChoice())
                .build();

        registerVote(vote);
    }

    @Override
    public List<Agenda> getAllUnpublishedAgendas() {
        var agendas = agendaRepository.findAll();
        return agendas.stream()
                .filter(agenda -> !agendaIsOpen(agenda))
                .collect(Collectors.toList());
    }

    private void verifyIfAssociateIsAbleToVote(VoteRequestDTO voteRequest) {
        var associateVotingStatus = associateValidatorClient.getAssociateVotingStatus(voteRequest.getAssociate());
        if (!associateVotingStatus.getStatus().equals("ABLE_TO_VOTE")) {
            logger.error("AgendaServiceImpl.voteAgenda - Error - Associate is not able to vote. " +
                    "- Associate identifier: {}", voteRequest.getAssociate());
            throw new AssociateIsNotAbleToVoteException();
        }
    }

    private int getDuration(AgendaRequestDTO agendaRequest) {
        return agendaRequest.getDuration() <= 0 ? DEFAULT_DURATION : agendaRequest.getDuration();
    }

    private void countVotes(AgendaResponseDTO agendaResponse) {
        List<Vote> votes = voteRepository.findAllByAgendaId(agendaResponse.getId());
        agendaResponse.setNegativeVotes(votes.stream()
                .filter(vote -> vote.getChoice().equals(NEGATIVE_VOTE)).count());
        agendaResponse.setPositiveVotes(votes.stream()
                .filter(vote -> vote.getChoice().equals(POSITIVE_VOTE)).count());
    }

    private void verifyIfAgendaHasAlreadyBeenClosed(Agenda agenda) {
        if (!agendaIsOpen(agenda) && agenda.getStartTime() != null) {
            logger.error("AgendaServiceImpl.verifyIfAgendaIsClosed - Error - Agenda has already been closed " +
                    "- Agenda identifier: {}", agenda.getId());
            throw new AgendaHasAlreadyBeenClosedException();
        }
    }

    private boolean agendaHasAlreadyBeenClosed(Agenda agenda) {
        return !agendaIsOpen(agenda) && agenda.getStartTime() != null;
    }

    private void verifyIfAgendaIsAlreadyOpen(Agenda agenda) {
        if (agendaIsOpen(agenda)) {
            logger.error("AgendaServiceImpl.verifyIfAgendaIsAlreadyOpen - Error - Agenda is already open " +
                    "- Agenda identifier: {}", agenda.getId());
            throw new AgendaIsAlreadyOpenException();
        }
    }

    private void verifyIfAssociateHaveNotVotedYet(String agendaId, VoteRequestDTO voteRequest) {
        if (voteRepository.findByAssociateAndAgendaId(voteRequest.getAssociate(), agendaId) != null) {
            logger.error("AgendaServiceImpl.verifyIfAssociateHaveNotVotedYet - Error - Associate already voted " +
                    "- Agenda identifier: {}, Associate identifier: {}", agendaId, voteRequest.getAssociate());
            throw new VoteAlreadyExistsException();
        }
    }

    private void verifyIfAgendaIsOpen(Agenda agenda) {
        if (!agendaIsOpen(agenda)) {
            logger.error("AgendaServiceImpl.verifyIfAgendaIsOpen - Error - Agenda is closed " +
                    "- Agenda identifier: {}", agenda.getId());
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
        logger.debug("AgendaServiceImpl.registerAgenda - Saved agenda with success - Agenda name: {}, Agenda identifier: {}", agenda.getName(), agenda.getId());
        return agenda;
    }

    private void registerVote(Vote vote) {
        voteRepository.insert(vote);
    }

    private Agenda verifyIfAgendaExist(String agendaId) {
        Optional<Agenda> agendaOptional = agendaRepository.findById(agendaId);

        return agendaOptional.orElseThrow(() -> {
            logger.error("AgendaServiceImpl.registerVote - Error - Agenda not found " +
                    "- Agenda identifier: {}", agendaId);
            throw new AgendaNotFoundException();
        });
    }

}
