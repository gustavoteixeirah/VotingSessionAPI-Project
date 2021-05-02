package dev.gustavoteixeira.api.votingsession.controller.agenda;

import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.exception.AgendaNotFoundException;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GetAgendaTest {
    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String NONEXISTENT_AGENDA_ID = "608ded0cc66aaf5bd61759de";
    public static final String AGENDA_NAME = "Rising gasoline tax by 3%";
    public static final int AGENDA_DURATION = 60;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AgendaService agendaService;

    @Test
    public void getAgendaWithExistingAgendaShouldReturnAnAgendaResponse() throws Exception {
        AgendaResponseDTO response = AgendaResponseDTO.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();

        when(agendaService.getAgenda(eq(AGENDA_ID))).thenReturn(response);

        mvc.perform(get("/agenda/"+AGENDA_ID))
                .andExpect(status().isOk());
    }

    @Test
    public void getAgendaThatDoesNotExistShouldReturnNotFound() throws Exception {
        when(agendaService.getAgenda(eq(NONEXISTENT_AGENDA_ID))).thenThrow(AgendaNotFoundException.class);

        mvc.perform(get("/agenda/"+ NONEXISTENT_AGENDA_ID))
                .andExpect(status().isNotFound());
    }


}
