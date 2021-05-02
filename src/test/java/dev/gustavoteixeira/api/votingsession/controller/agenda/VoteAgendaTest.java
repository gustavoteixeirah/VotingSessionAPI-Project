package dev.gustavoteixeira.api.votingsession.controller.agenda;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaClosedException;
import dev.gustavoteixeira.api.votingsession.exception.AgendaNotFoundException;
import dev.gustavoteixeira.api.votingsession.exception.VoteAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class VoteAgendaTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String NONEXISTENT_AGENDA_ID = "608ded0cc66aaf5bd61759de";
    public static final String ASSOCIATE_IDENTIFIER = "38347541027";
    public static final String POSITIVE_CHOICE = "SIM";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AgendaService agendaService;

    @Test
    void voteAgendaWithValidRequestShouldReturnOK() throws Exception {
        VoteRequestDTO requestBody = VoteRequestDTO.builder()
                .associate("validCPF")
                .choice(POSITIVE_CHOICE).build();

        when(agendaService.voteAgenda(eq(AGENDA_ID), any(VoteRequestDTO.class)))
                .thenReturn(VoteResponseDTO.builder().build());

        mvc.perform(post("/agenda/".concat(AGENDA_ID).concat("/vote"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isOk());
    }

    @Test
    void voteAgendaWithNonexistentAgendaRequestShouldReturnNotFound() throws Exception {
        VoteRequestDTO requestBody = VoteRequestDTO.builder()
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE).build();

        when(agendaService.voteAgenda(eq(NONEXISTENT_AGENDA_ID), any(VoteRequestDTO.class)))
                .thenThrow(AgendaNotFoundException.class);

        mvc.perform(post("/agenda/".concat(NONEXISTENT_AGENDA_ID).concat("/vote"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isNotFound());
    }

    @Test
    void voteAgendaWithClosedAgendaRequestShouldReturnNotAcceptable() throws Exception {
        VoteRequestDTO requestBody = VoteRequestDTO.builder()
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE).build();

        when(agendaService.voteAgenda(eq(NONEXISTENT_AGENDA_ID), any(VoteRequestDTO.class)))
                .thenThrow(AgendaClosedException.class);

        mvc.perform(post("/agenda/".concat(NONEXISTENT_AGENDA_ID).concat("/vote"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    void voteAgendaWithVoteAlreadyRegisteredRequestShouldReturnNotAcceptable() throws Exception {
        VoteRequestDTO requestBody = VoteRequestDTO.builder()
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE).build();

        when(agendaService.voteAgenda(eq(AGENDA_ID), any(VoteRequestDTO.class)))
                .thenThrow(VoteAlreadyExistsException.class);

        mvc.perform(post("/agenda/".concat(AGENDA_ID).concat("/vote"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(requestBody)))
                .andExpect(status().isNotAcceptable());
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

}
