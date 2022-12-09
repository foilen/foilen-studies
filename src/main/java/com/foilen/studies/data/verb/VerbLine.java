package com.foilen.studies.data.verb;

import com.foilen.studies.data.vocabulary.SpeakText;

public class VerbLine {

    private String pronoun;
    private String word;

    private SpeakText speakText;

    public String getPronoun() {
        return pronoun;
    }

    public void setPronoun(String pronoun) {
        this.pronoun = pronoun;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public SpeakText getSpeakText() {
        return speakText;
    }

    public void setSpeakText(SpeakText speakText) {
        this.speakText = speakText;
    }
}
