package dev.gustavoteixeira.api.votingsession.converter;

import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Vote;

public class VoteConverter {

    public static VoteResponseDTO getVoteResponse(Vote vote) {
        return VoteResponseDTO.builder()
                .associate(vote.getAssociate())
                .choice(vote.getChoice())
                .agendaId(vote.getAgendaId())
                .build();
    }

}
