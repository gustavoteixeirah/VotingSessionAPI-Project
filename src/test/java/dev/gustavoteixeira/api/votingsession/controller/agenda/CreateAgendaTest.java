package dev.gustavoteixeira.api.votingsession.controller.agenda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CreateAgendaTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String AGENDA_NAME = "Rising gasoline tax by 3%";
    public static final int AGENDA_DURATION = 60;
    public static final int DEFAULT_DURATION = 1;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AgendaService agendaService;

    @Test
    void creatingNewAgendaShouldReturnStatusCreated() throws Exception {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION)
                .startTime(LocalDateTime.now()).build();

        when(agendaService.createAgenda(any(AgendaRequestDTO.class))).thenReturn(agenda);

        AgendaRequestDTO requestBody = AgendaRequestDTO.builder()
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/agenda/608d817df3117478ca0f7432"));
    }

    @Test
    void creatingNewAgendaWithNameThatAlreadyExistsShouldReturnBadRequest() throws Exception {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION)
                .startTime(LocalDateTime.now()).build();

        when(agendaService.createAgenda(any(AgendaRequestDTO.class))).thenThrow(AgendaAlreadyExistsException.class);

        AgendaRequestDTO requestBody = AgendaRequestDTO.builder()
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION).build();

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creatingNewAgendaWithEmptyNameShouldReturnStatusBadRequest() throws Exception {
        AgendaRequestDTO requestBody = AgendaRequestDTO.builder()
                .name("")
                .duration(AGENDA_DURATION).build();

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creatingNewAgendaWithoutNameShouldReturnStatusBadRequest() throws Exception {
        AgendaRequestDTO requestBody = AgendaRequestDTO.builder()
                .duration(AGENDA_DURATION).build();

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creatingNewAgendaWithNegativeDurationShouldReturnStatusCreated() throws Exception {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(DEFAULT_DURATION).build();

        when(agendaService.createAgenda(any(AgendaRequestDTO.class))).thenReturn(agenda);

        AgendaRequestDTO requestBody = AgendaRequestDTO.builder()
                .name(AGENDA_NAME)
                .duration(-1).build();

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isCreated());
    }

    @Test
    void creatingNewAgendaWithZeroDurationShouldReturnStatusCreated() throws Exception {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(DEFAULT_DURATION)
                .startTime(LocalDateTime.now()).build();

        when(agendaService.createAgenda(any(AgendaRequestDTO.class))).thenReturn(agenda);

        AgendaRequestDTO requestBody = AgendaRequestDTO.builder()
                .name(AGENDA_NAME)
                .duration(0).build();

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isCreated());
    }

    @Test
    void creatingNewAgendaWithoutDurationShouldReturnStatusCreated() throws Exception {
        Agenda agenda = Agenda.builder()
                .id(AGENDA_ID)
                .name(AGENDA_NAME)
                .duration(AGENDA_DURATION)
                .startTime(LocalDateTime.now()).build();

        when(agendaService.createAgenda(any(AgendaRequestDTO.class))).thenReturn(agenda);

        AgendaRequestDTO requestBody = AgendaRequestDTO.builder()
                .name(AGENDA_NAME).build();

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isCreated());
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
