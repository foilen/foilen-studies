package com.foilen.studies.data;

import com.foilen.studies.data.verb.Verb;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.stream.Stream;

public interface VerbRepository extends MongoRepository<Verb, String> {
    Stream<Verb> findAllByOwnerUserIdOrderByName(String userId);

    Verb findByIdAndOwnerUserId(String id, String userId);

    Verb findFirstByVerbLinesSpeakTextCacheId(String cacheId);
}
