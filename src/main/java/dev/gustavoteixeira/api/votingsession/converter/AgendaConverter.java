package dev.gustavoteixeira.api.votingsession.converter;

import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;

import static dev.gustavoteixeira.api.votingsession.service.impl.AgendaServiceImpl.agendaIsOpen;

public class AgendaConverter {

    private AgendaConverter() {
        throw new IllegalStateException("Utility class");
    }

    public static AgendaResponseDTO getAgendaResponse(Agenda agenda) {
        return AgendaResponseDTO.builder()
                .id(agenda.getId())
                .name(agenda.getName())
                .duration(agenda.getDuration())
                .startTime(agenda.getStartTime() == null ? null : agenda.getStartTime())
                .isOpened(agendaIsOpen(agenda))
                .build();
    }

}
