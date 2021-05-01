package dev.gustavoteixeira.api.votingsession.repository;

import dev.gustavoteixeira.api.votingsession.entity.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {

    List<Vote> findAllByAgendaId(String agendaId);

    Vote findByAssociateAndAgendaId(String associate, String agendaId);
}
