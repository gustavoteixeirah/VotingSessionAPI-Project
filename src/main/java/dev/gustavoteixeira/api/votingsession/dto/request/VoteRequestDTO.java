package dev.gustavoteixeira.api.votingsession.dto.request;

import lombok.*;

import javax.validation.constraints.Pattern;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteRequestDTO {

    @Pattern(regexp = "[0-9]{11}")
    private String associate;

    @Pattern(regexp = "^(?:Sim|Não)$")
    private String choice;

}
