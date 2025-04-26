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

    @Test
    void extractFirstSentence_singleSentence() {
        var text = "This is a single sentence.";
        var result = AiGenerationServiceImpl.extractFirstSentence(text);
        Assertions.assertEquals(text, result);
    }

    @Test
    void extractFirstSentence_multipleSentences() {
        var text = "This is the first sentence. This is the second sentence. This is the third sentence.";
        var expected = "This is the first sentence.";
        var result = AiGenerationServiceImpl.extractFirstSentence(text);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void extractFirstSentence_differentPunctuation() {
        var text = "Is this a question? This is a statement. This is an exclamation!";
        var expected = "Is this a question?";
        var result = AiGenerationServiceImpl.extractFirstSentence(text);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void extractFirstSentence_noEndingPunctuation() {
        var text = "This sentence has no ending punctuation";
        var result = AiGenerationServiceImpl.extractFirstSentence(text);
        Assertions.assertEquals(text, result);
    }

}
