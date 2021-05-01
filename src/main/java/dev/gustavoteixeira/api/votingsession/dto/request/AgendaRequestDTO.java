package dev.gustavoteixeira.api.votingsession.dto.request;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendaRequestDTO {

    private String name;

    private int duration;

}
