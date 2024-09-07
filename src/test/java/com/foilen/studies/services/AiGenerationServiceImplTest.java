package com.foilen.studies.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AiGenerationServiceImplTest {

    @Test
    void checkWordInSentence_fail_bigger() {
        var word = "certain";
        var sentence = "La petite fille certaine de sa réussite, sourit fièrement à l'ensemble de son école.";
        Assertions.assertFalse(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

    @Test
    void checkWordInSentence_ok_spaces() {
        var word = "deuxième";
        var sentence = "La deuxième fois que j'ai allumé le feu de camp, j'ai ressenti une grande sensation de calme et de relaxation.";
        Assertions.assertTrue(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

    @Test
    void checkWordInSentence_ok_apostrophe_included() {
        var word = "l'été";
        var sentence = "Il fait chaud l'été.";
        Assertions.assertTrue(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

    @Test
    void checkWordInSentence_ok_apostrophe() {
        var word = "été";
        var sentence = "Il fait chaud l'été.";
        Assertions.assertTrue(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

    @Test
    void checkWordInSentence_ok_firstWord() {
        var word = "deuxième";
        var sentence = "Deuxième fois que j'ai allumé le feu de camp, j'ai ressenti une grande sensation de calme et de relaxation.";
        Assertions.assertTrue(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

    @Test
    void checkWordInSentence_ok_lastWord() {
        var word = "deuxième";
        var sentence = "Je suis arrivé en deuxième";
        Assertions.assertTrue(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

    @Test
    void checkWordInSentence_ok_lastWord_dot() {
        var word = "deuxième";
        var sentence = "Je suis arrivé en deuxième.";
        Assertions.assertTrue(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

    @Test
    void checkWordInSentence_ok_coma() {
        var word = "deuxième";
        var sentence = "Je suis arrivé en deuxième, et j'ai été très fier de moi.";
        Assertions.assertTrue(AiGenerationServiceImpl.checkWordInSentence(word, sentence));
    }

}