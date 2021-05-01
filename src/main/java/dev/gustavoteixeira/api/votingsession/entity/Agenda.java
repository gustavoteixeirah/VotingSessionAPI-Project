package dev.gustavoteixeira.api.votingsession.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "agendas")
@Getter
@Setter
@Builder
public class Agenda {

    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private LocalDateTime startTime;

    private int duration;

}
