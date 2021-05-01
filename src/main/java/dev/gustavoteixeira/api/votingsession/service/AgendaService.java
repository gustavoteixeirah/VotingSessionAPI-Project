package dev.gustavoteixeira.api.votingsession.service;

import dev.gustavoteixeira.api.votingsession.dto.request.AgendaRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.request.VoteRequestDTO;
import dev.gustavoteixeira.api.votingsession.dto.response.AgendaResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import dev.gustavoteixeira.api.votingsession.entity.Vote;

public interface AgendaService {

    Agenda createAgenda(AgendaRequestDTO agendaRequest);

    AgendaResponseDTO getAgenda(String agendaId);

    void startAgenda(String agendaId);

    Vote voteAgenda(String agendaId, VoteRequestDTO voteRequest);
}
