package dev.gustavoteixeira.api.votingsession.service.agenda;

import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.entity.Vote;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
import dev.gustavoteixeira.api.votingsession.repository.VoteRepository;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class VoteAgendaTest {

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
    void startAgendaWithValidAgendaIdShouldUpdateTheAgendaStartTime() {
        VoteRequestDTO voteRequest = VoteRequestDTO.builder()
                .associate(ASSOCIATE_IDENTIFIER_1)
                .choice(POSITIVE_CHOICE).build();
        Vote vote = Vote.builder()
                .associate(ASSOCIATE_IDENTIFIER_1)
                .choice(POSITIVE_CHOICE)
                .agendaId(AGENDA_ID).build();
        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(5));
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(AGENDA_ID)).thenReturn(agendaOptional);
        when(voteRepository.findByAssociateAndAgendaId(ASSOCIATE_IDENTIFIER_1, AGENDA_ID))
                .thenReturn(null);
        when(voteRepository.insert(any(Vote.class)))
                .thenReturn(vote);

        VoteResponseDTO result = agendaService.voteAgenda(AGENDA_ID, voteRequest);

        assertThat(result).isNotNull();
        verify(voteRepository, times(1)).insert(any(Vote.class));
        assertThat(result.getAgendaId()).isEqualTo(AGENDA_ID);
        assertThat(result.getAssociate()).isEqualTo(ASSOCIATE_IDENTIFIER_1);
        assertThat(result.getChoice()).isEqualTo(POSITIVE_CHOICE);
    }

    private Agenda getAgenda() {
        return Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();
    }
}
