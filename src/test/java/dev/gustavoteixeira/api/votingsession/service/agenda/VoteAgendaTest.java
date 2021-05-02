package dev.gustavoteixeira.api.votingsession.service.agenda;

import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.entity.Vote;
import dev.gustavoteixeira.api.votingsession.exception.AgendaClosedException;
import dev.gustavoteixeira.api.votingsession.exception.VoteAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.api.votingsession.repository.VoteRepository;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class VoteAgendaTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String AGENDA_NAME = "Rising gasoline tax by 3%";
    public static final int AGENDA_DURATION = 10;
    public static final String ASSOCIATE_IDENTIFIER = "38347541027";
    public static final String POSITIVE_CHOICE = "SIM";

    @Autowired
    private AgendaService agendaService;

    @MockBean
    private AgendaRepository agendaRepository;

    @MockBean
    private VoteRepository voteRepository;

    @Test
    void voteInAgendaThatIsOpenShouldRegisterVote() {
        VoteRequestDTO voteRequest = VoteRequestDTO.builder()
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE).build();
        Vote vote = Vote.builder()
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE)
                .agendaId(AGENDA_ID).build();
        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(5));
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(AGENDA_ID)).thenReturn(agendaOptional);
        when(voteRepository.findByAssociateAndAgendaId(ASSOCIATE_IDENTIFIER, AGENDA_ID))
                .thenReturn(null);
        when(voteRepository.insert(any(Vote.class)))
                .thenReturn(vote);

        agendaService.voteAgenda(AGENDA_ID, voteRequest);

        verify(voteRepository, times(1)).insert(any(Vote.class));
    }

    @Test
    void voteInAgendaThatIsClosedShouldReturnAgendaClosedException() {
        VoteRequestDTO voteRequest = VoteRequestDTO.builder()
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE).build();

        Agenda agenda = getAgenda();
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(AGENDA_ID)).thenReturn(agendaOptional);

        RuntimeException exception = Assertions.assertThrows(AgendaClosedException.class,
                () -> agendaService.voteAgenda(AGENDA_ID, voteRequest));

        assertThat(exception).isInstanceOf(AgendaClosedException.class);
    }

    @Test
    void voteInAgendaThatHasAlreadyVotedShouldReturnVoteAlreadyExistsException() {
        VoteRequestDTO voteRequest = VoteRequestDTO.builder()
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE).build();

        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(5));
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(AGENDA_ID)).thenReturn(agendaOptional);
        when(voteRepository.findByAssociateAndAgendaId(ASSOCIATE_IDENTIFIER, AGENDA_ID))
                .thenReturn(Vote.builder().build());

        RuntimeException exception = Assertions.assertThrows(VoteAlreadyExistsException.class,
                () -> agendaService.voteAgenda(AGENDA_ID, voteRequest));

        assertThat(exception).isInstanceOf(VoteAlreadyExistsException.class);
    }

    private Agenda getAgenda() {
        return Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();
    }
}
