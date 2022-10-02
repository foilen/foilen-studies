package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.Word;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface WordRepository extends MongoRepository<Word, String> {
    List<Word> findAllByOwnerUserIdAndWordIn(String ownerUserId, List<String> words);
}
