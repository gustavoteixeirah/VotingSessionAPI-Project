package dev.gustavoteixeira.api.votingsession.service;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;

public interface AgendaService {

    Agenda createAgenda(AgendaRequestDTO agendaRequest);

    AgendaResponseDTO getAgenda(String agendaId);

    void startAgenda(String agendaId);

    void voteAgenda(String agendaId, VoteRequestDTO voteRequest);
}
