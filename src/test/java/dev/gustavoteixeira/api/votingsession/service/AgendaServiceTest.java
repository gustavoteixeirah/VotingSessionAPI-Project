package dev.gustavoteixeira.api.votingsession.service;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.exception.AgendaAlreadyExistsException;
import dev.gustavoteixeira.api.votingsession.repository.AgendaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DuplicateKeyException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AgendaServiceTest {

    @Autowired
    private AgendaService agendaService;

    @MockBean
    private AgendaRepository agendaRepository;

    @Test
    public void createNewAgenda() {
        when(agendaRepository.insert(any(Agenda.class))).thenReturn(Agenda.builder().build());
        agendaService.createAgenda(getAgendaDTO());
    }

    @Test
    public void createNewAgendaWithNameThatAlreadyExistsShouldReturnAgendaAlreadyExistsException() {
        doThrow(DuplicateKeyException.class).when(agendaRepository).insert(any(Agenda.class));

        RuntimeException exception = Assertions.assertThrows(AgendaAlreadyExistsException.class,
                () -> agendaService.createAgenda(getAgendaDTO()));

        assertThat(exception).isInstanceOf(AgendaAlreadyExistsException.class);
    }

    private AgendaRequestDTO getAgendaDTO() {
        return AgendaRequestDTO.builder()
                .name("Aumento no imposto da gasolina")
                .duration(60)
                .build();
    }

}
