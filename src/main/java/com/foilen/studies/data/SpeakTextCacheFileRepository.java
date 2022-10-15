package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.SpeakTextCacheFile;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Set;

public interface SpeakTextCacheFileRepository extends MongoRepository<SpeakTextCacheFile, String> {

    SpeakTextCacheFile findByCacheId(String cacheId);

    Set<SpeakTextCacheFile> findAllByCacheIdIn(Set<String> cacheIds);

    Set<SpeakTextCacheFile> findAllByCacheIdNotIn(Set<String> allSpeakTextCacheFileIds);
}
