package dev.gustavoteixeira.api.votingsession.dto.response;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteResponseDTO {

    private String associate;
    private String choice;
    private String agendaId;

}
