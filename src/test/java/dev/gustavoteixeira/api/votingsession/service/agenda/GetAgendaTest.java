package dev.gustavoteixeira.api.votingsession.service.agenda;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.entity.Vote;
import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaNotFoundException;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.api.votingsession.repository.VoteRepository;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@SpringBootTest
public class GetAgendaTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String NONEXISTENT_AGENDA_ID = "608ded0cc66aaf5bd61759de";
    public static final String AGENDA_NAME = "Rising gasoline tax by 3%";
    public static final int AGENDA_DURATION = 10;
    public static final String ASSOCIATE_IDENTIFIER_1 = "38347541027";
    public static final String ASSOCIATE_IDENTIFIER_2 = "72545153001";
    public static final String ASSOCIATE_IDENTIFIER_3 = "10360912010";
    public static final String POSITIVE_CHOICE = "SIM";
    public static final String NEGATIVE_CHOICE = "NÂO";

    @Autowired
    private AgendaService agendaService;

    @MockBean
    private AgendaRepository agendaRepository;

    @MockBean
    private VoteRepository voteRepository;

    @Test
    public void getAgendaWithValidAgendaIdShouldReturnAgendaResponseDTO() {
        Optional<Agenda> agendaOptional = Optional.of(getAgenda());
        when(agendaRepository.findById(eq(AGENDA_ID))).thenReturn(agendaOptional);

        AgendaResponseDTO result = agendaService.getAgenda(AGENDA_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(AGENDA_ID);
        assertThat(result.getName()).isEqualTo(AGENDA_NAME);
        assertThat(result.getDuration()).isEqualTo(AGENDA_DURATION);
        assertThat(result.getPositiveVotes()).isZero();
        assertThat(result.getNegativeVotes()).isZero();
        assertThat(result.getStartTime()).isNull();
        assertThat(result.isOpened()).isFalse();
    }

    @Test
    public void getAgendaWithVotingInProgressShouldReturnAgendaResponseDTO() {
        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(5));
        Optional<Agenda> agendaOptional = Optional.of(agenda);
        when(agendaRepository.findById(eq(AGENDA_ID))).thenReturn(agendaOptional);

        AgendaResponseDTO result = agendaService.getAgenda(AGENDA_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(AGENDA_ID);
        assertThat(result.getName()).isEqualTo(AGENDA_NAME);
        assertThat(result.getDuration()).isEqualTo(AGENDA_DURATION);
        assertThat(result.getPositiveVotes()).isZero();
        assertThat(result.getNegativeVotes()).isZero();
        assertThat(result.getStartTime()).isNotNull();
        assertThat(result.isOpened()).isTrue();
    }

    @Test
    public void getAgendaWithVotingInProgressAndWithVotesShouldReturnAgendaResponseDTO() {
        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(5));
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(eq(AGENDA_ID))).thenReturn(agendaOptional);
        when(voteRepository.findAllByAgendaId(eq(AGENDA_ID))).thenReturn(getVoteList());

        AgendaResponseDTO result = agendaService.getAgenda(AGENDA_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(AGENDA_ID);
        assertThat(result.getName()).isEqualTo(AGENDA_NAME);
        assertThat(result.getDuration()).isEqualTo(AGENDA_DURATION);
        assertThat(result.getPositiveVotes()).isEqualTo(2);
        assertThat(result.getNegativeVotes()).isEqualTo(1);
        assertThat(result.getStartTime()).isNotNull();
        assertThat(result.isOpened()).isTrue();
    }

    @Test
    public void getAgendaWithVotingClosedAndWithVotesShouldReturnAgendaResponseDTO() {
        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(10));
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(eq(AGENDA_ID))).thenReturn(agendaOptional);
        when(voteRepository.findAllByAgendaId(eq(AGENDA_ID))).thenReturn(getVoteList());

        AgendaResponseDTO result = agendaService.getAgenda(AGENDA_ID);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(AGENDA_ID);
        assertThat(result.getName()).isEqualTo(AGENDA_NAME);
        assertThat(result.getDuration()).isEqualTo(AGENDA_DURATION);
        assertThat(result.getPositiveVotes()).isEqualTo(2);
        assertThat(result.getNegativeVotes()).isEqualTo(1);
        assertThat(result.getStartTime()).isNotNull();
        assertThat(result.isOpened()).isFalse();
    }

    @Test
    public void getAgendaWithNonexistentAgendaIdShouldThrowAgendaNotFoundException() {
        Optional<Agenda> agendaOptional = Optional.empty();

        when(agendaRepository.findById(eq(NONEXISTENT_AGENDA_ID))).thenReturn(agendaOptional);

        RuntimeException exception = Assertions.assertThrows(AgendaNotFoundException.class,
                () -> agendaService.getAgenda(NONEXISTENT_AGENDA_ID));

        assertThat(exception).isInstanceOf(AgendaNotFoundException.class);
    }

    private Agenda getAgenda() {
        return Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();
    }

    private List<Vote> getVoteList() {
        List<Vote> votes = new ArrayList<>();
        votes.add(getVote(ASSOCIATE_IDENTIFIER_1, POSITIVE_CHOICE));
        votes.add(getVote(ASSOCIATE_IDENTIFIER_2, POSITIVE_CHOICE));
        votes.add(getVote(ASSOCIATE_IDENTIFIER_3, NEGATIVE_CHOICE));
        return votes;
    }

    private Vote getVote(String associateIdentifier, String choice) {
        return Vote.builder()
                .associate(associateIdentifier)
                .choice(choice).build();
    }


}
