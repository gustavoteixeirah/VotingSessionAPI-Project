package dev.gustavoteixeira.api.votingsession.service.agenda;

import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.exception.AgendaHasAlreadyBeenClosedException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaIsAlreadyOpenException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaNotFoundException;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
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
class StartAgendaTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String NONEXISTENT_AGENDA_ID = "608ded0cc66aaf5bd61759de";
    public static final String AGENDA_NAME = "Rising gasoline tax by 3%";
    public static final int AGENDA_DURATION = 10;

    @Autowired
    private AgendaService agendaService;

    @MockBean
    private AgendaRepository agendaRepository;

    @Test
    void startAgendaWithValidAgendaIdShouldUpdateTheAgendaStartTime() {
        Agenda agenda = getAgenda();
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(AGENDA_ID)).thenReturn(agendaOptional);

        agendaService.startAgenda(AGENDA_ID);

        verify(agendaRepository, times(1)).save(any(Agenda.class));
    }

    @Test
    void startAgendaWithInvalidAgendaIdShouldThrowAgendaNotFoundException() {
        Optional<Agenda> agendaOptional = Optional.empty();

        when(agendaRepository.findById(NONEXISTENT_AGENDA_ID)).thenReturn(agendaOptional);

        RuntimeException exception = Assertions.assertThrows(AgendaNotFoundException.class,
                () -> agendaService.startAgenda(NONEXISTENT_AGENDA_ID));

        assertThat(exception).isInstanceOf(AgendaNotFoundException.class);
    }

    @Test
    void startAgendaAlreadyOpenedAgendaShouldThrowAgendaIsAlreadyOpenException() {
        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(1));
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(AGENDA_ID)).thenReturn(agendaOptional);

        RuntimeException exception = Assertions.assertThrows(AgendaIsAlreadyOpenException.class,
                () -> agendaService.startAgenda(AGENDA_ID));

        assertThat(exception).isInstanceOf(AgendaIsAlreadyOpenException.class);
    }

    @Test
    void startAgendaThatIsClosedShouldThrowAgendaHasAlreadyBeenClosedException() {
        Agenda agenda = getAgenda();
        agenda.setStartTime(LocalDateTime.now().minusMinutes(10));
        Optional<Agenda> agendaOptional = Optional.of(agenda);

        when(agendaRepository.findById(AGENDA_ID)).thenReturn(agendaOptional);

        RuntimeException exception = Assertions.assertThrows(AgendaHasAlreadyBeenClosedException.class,
                () -> agendaService.startAgenda(AGENDA_ID));

        assertThat(exception).isInstanceOf(AgendaHasAlreadyBeenClosedException.class);
    }

    private Agenda getAgenda() {
        return Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();
    }

}
