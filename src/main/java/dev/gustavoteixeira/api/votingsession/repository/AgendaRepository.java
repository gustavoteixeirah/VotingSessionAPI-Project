package dev.gustavoteixeira.api.votingsession.repository;

import dev.gustavoteixeira.api.votingsession.entity.Agenda;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgendaRepository extends MongoRepository<Agenda, String> {
}
