package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.DivisionScores;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DivisionScoresRepository extends MongoRepository<DivisionScores, String> {
}
