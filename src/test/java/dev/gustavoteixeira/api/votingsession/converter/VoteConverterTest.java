package dev.gustavoteixeira.api.votingsession.converter;

import dev.gustavoteixeira.api.votingsession.dto.response.VoteResponseDTO;
import dev.gustavoteixeira.api.votingsession.entity.Vote;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static dev.gustavoteixeira.api.votingsession.converter.VoteConverter.getVoteResponse;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VoteConverterTest {

    public static final String AGENDA_ID = "608d817df3117478ca0f7432";
    public static final String ASSOCIATE_IDENTIFIER = "38347541027";
    public static final String POSITIVE_CHOICE = "SIM";

    @Test
    void getAgendaResponseWithNewlyCreatedShouldReturnAgendaResponseDTO() {
        Vote vote = Vote.builder()
                .agendaId(AGENDA_ID)
                .associate(ASSOCIATE_IDENTIFIER)
                .choice(POSITIVE_CHOICE).build();

        VoteResponseDTO result = getVoteResponse(vote);

        assertThat(result).isNotNull();
        assertThat(result.getAgendaId()).isEqualTo(AGENDA_ID);
        assertThat(result.getAssociate()).isEqualTo(ASSOCIATE_IDENTIFIER);
        assertThat(result.getChoice()).isEqualTo(POSITIVE_CHOICE);
    }

}
