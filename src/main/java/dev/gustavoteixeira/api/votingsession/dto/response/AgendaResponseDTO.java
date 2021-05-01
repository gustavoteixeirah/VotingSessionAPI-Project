package dev.gustavoteixeira.api.votingsession.dto.response;


import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AgendaResponseDTO {

    private String id;

    private String name;

    private LocalDateTime startTime;

    private int duration;

    private boolean isOpened;

    private long positiveVotes;

    private long negativeVotes;

}
