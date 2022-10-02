package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.WordList;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WordListRepository extends MongoRepository<WordList, String> {
}
