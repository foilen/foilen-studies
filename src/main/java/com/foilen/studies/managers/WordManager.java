package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.RandomWordListForm;
import com.foilen.studies.controllers.models.TrackForm;
import com.foilen.studies.controllers.models.WordListExpended;
import com.foilen.studies.controllers.models.WordListWithScore;
import com.foilen.studies.data.vocabulary.Word;

import java.util.List;

public interface WordManager {

    List<Word> bulkSplit(String userId, String wordsInText, boolean acceptSpacesInWords);

    FormResult copyWordList(String userId, String fromWordListId, String toWordListId);

    List<Word> randomWord(String userId, RandomWordListForm form);

    FormResult track(String userId, TrackForm form);

    List<WordListWithScore> listWordList(String userId);

    FormResult saveWordList(String userId, WordListExpended form);

    FormResult deleteWordList(String userId, String wordListId);

    WordListExpended getWordListExpended(String userId, String wordListId);

    void generateSentenceForWordsWithoutOne();

    void generateSentenceForWordsWithoutOneAddMoreIfNoneOnTheQueue();

}
