package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.MultiplicationScores;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MultiplicationScoresRepository extends MongoRepository<MultiplicationScores, String> {
}
