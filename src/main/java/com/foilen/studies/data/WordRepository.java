package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.Word;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

public interface WordRepository extends MongoRepository<Word, String> {
    Word findFirstBySpeakTextCacheId(String cacheId);

    Stream<Word> findAllBySpeakTextCacheIdIsNull();

    List<Word> findAllByOwnerUserIdAndIdIn(String userId, Collection<String> ids);

    List<Word> findAllByOwnerUserIdAndWordIn(String ownerUserId, List<String> words);
}
