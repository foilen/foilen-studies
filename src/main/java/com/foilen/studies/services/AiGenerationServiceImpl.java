package com.foilen.studies.services;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.BufferBatchesTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@Service
public class AiGenerationServiceImpl extends AbstractBasics implements AiGenerationService {

    private static final Set<Character> SEPARATORS_WITH_SPACE = new HashSet<>(Arrays.asList('\n', '\r', '\t', '.', ',', '/', ' ', '+', '*', '\\', '|', ';', '!', '?', '(', ')', '\''));

    public static final String SENTENCES_SYSTEM_PROMPT = """
            - As an elementary school teacher, create one dictation sentence per word in the provided list.
            - Each word must appear in its sentence written as is with the same gender and number. When it is a verb, keep it in the infinitive form or in the conjugation form to stay the same.
            - The sentences must be in {LANG}.
            - Output exactly one line per word, in the same order, using this exact format: <word>: <sentence>
            - Output only those lines, no other text or formatting.
            """;

    @Autowired
    private ChatClient chatClient;

    @Override
    public Map<String, String> generateSentences(Locale locale, List<String> words) {
        Map<String, String> results = new LinkedHashMap<>();
        BufferBatchesTools.<String>autoClose(10,
                batch -> {
                    logger.info("Generating sentences for batch of {} words in locale: {}", batch, locale.getDisplayLanguage());
                    String userMessage = String.join("\n", batch);
                    String response = chatClient.prompt()
                            .system(SENTENCES_SYSTEM_PROMPT.replace("{LANG}", locale.getDisplayLanguage()))
                            .user(userMessage)
                            .call()
                            .content();
                    results.putAll(parseSentencesResponse(batch, response));
                },
                list -> list.add(words)
        );
        return results;
    }

    protected static Map<String, String> parseSentencesResponse(List<String> words, String response) {
        Map<String, String> results = new LinkedHashMap<>();
        for (String line : response.split("\n")) {
            line = line.trim();
            if (line.isEmpty()) {
                continue;
            }
            int colonIndex = line.indexOf(": ");
            if (colonIndex == -1) {
                continue;
            }
            String word = line.substring(0, colonIndex).trim();
            String sentence = line.substring(colonIndex + 2).trim();
            if (!words.contains(word)) {
                continue;
            }
            if (!checkWordInSentence(word, sentence)) {
                throw new RuntimeException("The sentence does not contain the word: " + word + " - " + sentence);
            }
            if (sentence.length() > 200) {
                throw new RuntimeException("The sentence is too long: " + word + " - " + sentence);
            }
            results.put(word, sentence);
        }
        return results;
    }

    protected static boolean checkWordInSentence(String word, String sentence) {
        word = word.toLowerCase();
        sentence = sentence.toLowerCase();

        int startIndexOfWord = 0;
        while ((startIndexOfWord = sentence.indexOf(word, startIndexOfWord)) != -1) {
            if (startIndexOfWord == 0 || SEPARATORS_WITH_SPACE.contains(sentence.charAt(startIndexOfWord - 1))) {
                int endIndexOfWord = startIndexOfWord + word.length();
                if (endIndexOfWord == sentence.length() || SEPARATORS_WITH_SPACE.contains(sentence.charAt(endIndexOfWord))) {
                    return true;
                }
            }
            startIndexOfWord += word.length();
        }

        return false;
    }

}
