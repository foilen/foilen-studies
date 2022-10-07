package com.foilen.studies.managers;

import com.foilen.studies.controllers.models.RandomWordListForm;
import com.foilen.studies.data.vocabulary.Word;
import com.foilen.studies.data.vocabulary.WordList;

import java.util.List;

public interface WordManager {
    void createWordList(String userId, String listName, String wordsInText);

    List<WordList> listWordList(String userId);

    List<Word> listWord(String userId, String wordListId);

    List<Word> randomWord(String userId, RandomWordListForm form);

}
