package dev.gustavoteixeira.api.votingsession.repository;

import dev.gustavoteixeira.api.votingsession.entity.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepository extends MongoRepository<Vote, String> {
}
