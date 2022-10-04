package com.foilen.studies.services;

import org.springframework.core.io.Resource;

public interface SpeechService {
    Resource getFile(String cacheId);
}
