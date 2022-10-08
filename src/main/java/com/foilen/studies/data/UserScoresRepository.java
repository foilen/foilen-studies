package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.UserScores;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserScoresRepository extends MongoRepository<UserScores, String> {

}
