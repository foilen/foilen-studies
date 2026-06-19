package com.foilen.studies.services;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public interface AiGenerationService {

    Map<String, String> generateSentences(Locale locale, List<String> words);

}
