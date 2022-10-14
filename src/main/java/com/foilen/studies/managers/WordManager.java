package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.RandomWordListForm;
import com.foilen.studies.controllers.models.TrackForm;
import com.foilen.studies.controllers.models.WordListExpended;
import com.foilen.studies.data.vocabulary.Word;
import com.foilen.studies.data.vocabulary.WordList;

import java.util.List;

public interface WordManager {

    List<Word> bulkSplit(String userId, String wordsInText);

    List<Word> randomWord(String userId, RandomWordListForm form);

    FormResult track(String userId, TrackForm form);

    List<WordList> listWordList(String userId);

    FormResult listWordSave(String userId, WordListExpended form);

    WordListExpended getWordListExpended(String userId, String wordListId);
}
