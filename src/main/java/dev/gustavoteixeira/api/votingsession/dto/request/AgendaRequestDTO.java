package dev.gustavoteixeira.api.votingsession.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendaRequestDTO {

    @NotBlank(message = "O nome não pode estar vazio.")
    private String name;

    private int duration;

}
