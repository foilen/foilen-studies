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
            - As an elementary school teacher, create a single dictation sentence that includes the following word, written as is with the same gender number. When it is a verb, keep it in the infinitive form or in the conjugation form to stay the same.
            - The sentence must be in {LANG}.
            - Only output a single sentence, not multiple sentences.
            """;

    @Autowired
    private ChatClient chatClient;

    /**
     * Extract only the first sentence from a text that might contain multiple sentences.
     * A sentence is considered to end with a period, exclamation mark, or question mark.
     *
     * @param text The text to extract the first sentence from
     * @return The first sentence
     */
    protected static String extractFirstSentence(String text) {
        int firstSentenceEnd = -1;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '.' || c == '!' || c == '?') {
                firstSentenceEnd = i + 1;
                break;
            }
        }

        if (firstSentenceEnd > 0) {
            return text.substring(0, firstSentenceEnd).trim();
        }

        return text;
    }

    @Override
    public String generateSentence(Locale locale, String word) {

        logger.info("Generating sentence for word: {} in locale: {}", word, locale.getDisplayLanguage());

        String sentence = chatClient.prompt()
                .system(SENTENCE_SYSTEM_PROMPT.replace("{LANG}", locale.getDisplayLanguage()))
                .user(word)
                .call()
                .content();

        // Extract only the first sentence if multiple sentences are generated
        String firstSentence = extractFirstSentence(sentence);
        if (!firstSentence.equals(sentence)) {
            logger.info("Extracted first sentence: {}", firstSentence);
            sentence = firstSentence;
        }

        // Check that the sentence is not too long (max 200 characters)
        if (sentence.length() > 200) {
            logger.error("The sentence is too long: {} - {}", sentence.length(), sentence);
            throw new RuntimeException("The sentence is too long");
        }

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
