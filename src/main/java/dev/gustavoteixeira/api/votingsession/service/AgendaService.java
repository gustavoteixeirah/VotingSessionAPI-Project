package dev.gustavoteixeira.api.votingsession.service;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;

public interface AgendaService {

    Agenda createAgenda(AgendaRequestDTO agendaRequest);

}
