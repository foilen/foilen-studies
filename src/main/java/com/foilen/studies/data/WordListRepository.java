package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.WordList;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WordListRepository extends MongoRepository<WordList, String> {
    WordList findByIdAndOwnerUserId(String id, String userId);

    List<WordList> findAllByOwnerUserId(String userId);

    List<WordList> findAllByOwnerUserIdOrderByName(String userId);
}
