package dev.gustavoteixeira.api.votingsession.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequestDTO {

    private String associate;
    private String choice;

}
