package dev.gustavoteixeira.api.votingsession.controller.agenda;

import dev.gustavoteixeira.api.votingsession.exception.AgendaHasAlreadyBeenClosedException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaIsAlreadyOpenException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaNotFoundException;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StartAgendaTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String NONEXISTENT_AGENDA_ID = "608ded0cc66aaf5bd61759de";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AgendaService agendaService;

    @Test
    public void startAgendaWithExistingAgendaIdShouldReturnOk() throws Exception {
        doNothing().when(agendaService).startAgenda(eq(AGENDA_ID));

        mvc.perform(patch("/agenda/".concat(AGENDA_ID).concat("/start")))
                .andExpect(status().isOk());
    }

    @Test
    public void startAgendaWithNonExistingAgendaIdShouldReturnNotFound() throws Exception {
        doThrow(AgendaNotFoundException.class).when(agendaService).startAgenda(eq(NONEXISTENT_AGENDA_ID));

        mvc.perform(patch("/agenda/".concat(NONEXISTENT_AGENDA_ID).concat("/start")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void startAgendaThatIsAlreadyOpenShouldReturnNotAcceptable() throws Exception {
        doThrow(AgendaIsAlreadyOpenException.class).when(agendaService).startAgenda(eq(AGENDA_ID));

        mvc.perform(patch("/agenda/".concat(AGENDA_ID).concat("/start")))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void startAgendaThatIsClosedShouldReturnNotAcceptable() throws Exception {
        doThrow(AgendaHasAlreadyBeenClosedException.class).when(agendaService).startAgenda(eq(AGENDA_ID));

        mvc.perform(patch("/agenda/".concat(AGENDA_ID).concat("/start")))
                .andExpect(status().isNotAcceptable());
    }

}
