package dev.gustavoteixeira.api.votingsession.converter;

import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static dev.gustavoteixeira.api.votingsession.converter.AgendaConverter.getAgendaResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AgendaConverterTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String AGENDA_NAME = "Rising gasoline tax by 3%";
    public static final int AGENDA_DURATION = 2;

    @Test
    void getAgendaResponseWithNewlyCreatedShouldReturnAgendaResponseDTO() {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();

        AgendaResponseDTO result = getAgendaResponse(agenda);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(AGENDA_ID);
        assertThat(result.getName()).isEqualTo(AGENDA_NAME);
        assertThat(result.getDuration()).isEqualTo(AGENDA_DURATION);
        assertThat(result.getStartTime()).isNull();
        assertThat(result.getPositiveVotes()).isZero();
        assertThat(result.getNegativeVotes()).isZero();
        assertThat(result.isOpened()).isFalse();
    }

    @Test
    void getAgendaResponseWithAgendaVotingInProgressShouldReturnAgendaResponseDTO() {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION)
                .startTime(LocalDateTime.now().minusMinutes(1)).build();

        AgendaResponseDTO result = getAgendaResponse(agenda);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(AGENDA_ID);
        assertThat(result.getName()).isEqualTo(AGENDA_NAME);
        assertThat(result.getDuration()).isEqualTo(AGENDA_DURATION);
        assertThat(result.getStartTime()).isNotNull();
        assertThat(result.getPositiveVotes()).isZero();
        assertThat(result.getNegativeVotes()).isZero();
        assertThat(result.isOpened()).isTrue();
    }

    @Test
    void getAgendaResponseWithAlreadyVotedAgendaShouldReturnAgendaResponseDTO() {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION)
                .startTime(LocalDateTime.now().minusMinutes(2)).build();

        AgendaResponseDTO result = getAgendaResponse(agenda);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(AGENDA_ID);
        assertThat(result.getName()).isEqualTo(AGENDA_NAME);
        assertThat(result.getDuration()).isEqualTo(AGENDA_DURATION);
        assertThat(result.getStartTime()).isNotNull();
        assertThat(result.getPositiveVotes()).isZero();
        assertThat(result.getNegativeVotes()).isZero();
        assertThat(result.isOpened()).isFalse();
    }

}
