package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.SpeakTextCacheFile;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SpeakTextCacheFileRepository extends MongoRepository<SpeakTextCacheFile, String> {

    SpeakTextCacheFile findByCacheId(String cacheId);

}
