package dev.gustavoteixeira.api.votingsession.controller;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.service.AgendaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AgendaControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AgendaService agendaService;

    @Test
    public void creatingNewAgendaShouldReturnStatusOK() throws Exception {
        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getAgendaDTO()))
        ).andExpect(status().isOk());
    }

    @Test
    public void creatingNewAgendaWithNameThatAlreadyExistsShouldReturnStatusBadRequest() throws Exception {
        doThrow(AgendaAlreadyExistsException.class).when(agendaService).createAgenda(any(AgendaRequestDTO.class));

        mvc.perform(post("/agenda")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapToJson(getAgendaDTO()))
        ).andExpect(status().isBadRequest());
    }

    private AgendaRequestDTO getAgendaDTO() {
        return AgendaRequestDTO.builder()
                .name("Aumento no imposto da gasolina")
                .duration(60)
                .build();
    }

    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }

    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }

}
