package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.Word;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.stream.Stream;

public interface WordRepository extends MongoRepository<Word, String> {
    Word findOneBySpeakTextCacheId(String cacheId);

    Stream<Word> findAllBySpeakTextCacheIdIsNull();

    List<Word> findAllByOwnerUserIdAndWordIn(String ownerUserId, List<String> words);
}
