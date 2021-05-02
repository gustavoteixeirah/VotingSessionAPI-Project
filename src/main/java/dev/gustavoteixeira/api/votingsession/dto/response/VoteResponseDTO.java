package dev.gustavoteixeira.api.votingsession.dto.response;

import lombok.*;

@Getter
@AllArgsConstructor
@Builder
public class VoteResponseDTO {

    private String associate;
    private String choice;
    private String agendaId;

}
