package dev.gustavoteixeira.api.votingsession.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class AgendaResultDTO {
    
    private String id;

    private String name;

    private long positiveVotes;

    private long negativeVotes;

}
