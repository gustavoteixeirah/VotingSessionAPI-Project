package dev.gustavoteixeira.api.votingsession.dto.request;

import com.mongodb.lang.Nullable;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

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
