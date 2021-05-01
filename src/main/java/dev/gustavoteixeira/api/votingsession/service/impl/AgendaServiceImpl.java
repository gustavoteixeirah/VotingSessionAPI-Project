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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AgendaServiceImpl implements AgendaService {

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

        agenda = registerAgenda(agenda);

        return agenda;
    }

    @Override
    public AgendaResponseDTO getAgenda(String agendaId) {
        Agenda agenda = verifyIfAgendaExist(agendaId);

        AgendaResponseDTO response = AgendaResponseDTO.builder()
                .id(agenda.getId())
                .name(agenda.getName())
                .positiveVotes(0)
                .startTime(agenda.getStartTime() == null ? null : agenda.getStartTime())
                .isOpened(agendaIsOpen(agenda))
                .build();

        //TODO metodo para contabilizar os votos


        //TODO Use object mapper
        return response;
    }

    @Override
    public void startAgenda(String agendaId) {
        Agenda agenda = verifyIfAgendaExist(agendaId);
        agenda.setStartTime(LocalDateTime.now());
        agendaRepository.save(agenda);
    }

    @Override
    public void voteAgenda(String agendaId, VoteRequestDTO voteRequest) {
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

        registerVote(vote);
    }

    private void verifyIfAgendaIsOpen(Agenda agenda) {
        if (!agendaIsOpen(agenda)) {
            throw new AgendaClosedException();
        }
    }

    private static boolean agendaIsOpen(Agenda agenda) {
        return agenda.getStartTime() != null && (agenda.getStartTime().plusMinutes(agenda.getDuration()).isAfter(LocalDateTime.now()));
    }

    private Agenda registerAgenda(Agenda agenda) {
        try {
            agenda = agendaRepository.insert(agenda);
        } catch (DuplicateKeyException e) {
            throw new AgendaAlreadyExistsException();
        }
        return agenda;
    }

    private void registerVote(Vote vote) {
        try {
            voteRepository.insert(vote);
        } catch (DuplicateKeyException e) {
            throw new VoteAlreadyExistsException();
        }
    }

    private Agenda verifyIfAgendaExist(String agendaId) {
        Optional<Agenda> agendaOptional = agendaRepository.findById(agendaId);
        return agendaOptional.orElseThrow(AgendaNotFoundException::new);
    }

}
