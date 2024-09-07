package com.foilen.studies.services;

import com.foilen.smalltools.tools.AbstractBasics;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class AiGenerationServiceImpl extends AbstractBasics implements AiGenerationService {

    private static final Set<Character> SEPARATORS_WITH_SPACE = new HashSet<>(Arrays.asList('\n', '\r', '\t', '.', ',', '/', ' ', '+', '*', '\\', '|', ';', '!', '?', '(', ')', '\''));

    public static final String SENTENCE_SYSTEM_PROMPT = """
            - As an elementary school teacher, create a dictation sentence that includes the following word, written as is with the same gender number. When it is a verb, keep it in the infinitive form or in the conjugation form to stay the same.
            - The sentence must be in {LANG}.
            - Only output the sentence.
            """;

    @Autowired
    private ChatClient chatClient;

    @Override
    public String generateSentence(Locale locale, String word) {

        logger.info("Generating sentence for word: {} in locale: {}", word, locale.getDisplayLanguage());

        String sentence = chatClient.prompt()
                .system(SENTENCE_SYSTEM_PROMPT.replace("{LANG}", locale.getDisplayLanguage()))
                .user(word)
                .call()
                .content();

        // Ensure the word is really in the sentence (case-insensitive)
        if (!checkWordInSentence(word, sentence)) {
            logger.error("The sentence does not contain the word: {} - {}", word, sentence);
            throw new RuntimeException("The sentence does not contain the word");
        }

        return sentence;
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
