package dev.gustavoteixeira.api.votingsession.service;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;

public interface AgendaService {

    void createAgenda(AgendaRequestDTO agendaRequest);

}
