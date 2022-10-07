package com.foilen.studies.managers;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.studies.controllers.models.RandomWordListForm;
import com.foilen.studies.data.WordListRepository;
import com.foilen.studies.data.WordRepository;
import com.foilen.studies.data.vocabulary.Language;
import com.foilen.studies.data.vocabulary.SpeakText;
import com.foilen.studies.data.vocabulary.Word;
import com.foilen.studies.data.vocabulary.WordList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class WordManagerImpl extends AbstractBasics implements WordManager {

    @Autowired
    private WordRepository wordRepository;
    @Autowired
    private WordListRepository wordListRepository;

    @Override
    public void createWordList(String userId, String listName, String wordsInText) {

        var wordNames = tokenize(wordsInText);
        logger.info("The list will have {} words", wordNames.size());

        // Get the words already created by me
        var existingWords = wordRepository.findAllByOwnerUserIdAndWordIn(userId, wordNames);
        logger.info("Found {} existing words", existingWords.size());

        // Create missing words
        var wordByName = existingWords.stream() //
                .collect(Collectors.toMap(Word::getWord, w -> w));
        for (var wordName : wordNames) {
            if (wordByName.containsKey(wordName)) {
                continue;
            }
            logger.info("Create word {}", wordName);
            var newWord = new Word();
            newWord.setOwnerUserId(userId);
            newWord.setWord(wordName);
            newWord.setSpeakText(new SpeakText(Language.FR, wordName));
            newWord = wordRepository.save(newWord);
            wordByName.put(wordName, newWord);
        }

        // Create the word list
        logger.info("Create list {}", listName);
        var wordList = new WordList();
        wordList.setName(listName);
        wordList.setOwnerUserId(userId);
        wordList.setWordIds(wordByName.values().stream().map(Word::getId).collect(Collectors.toList()));
        wordListRepository.save(wordList);
    }

    @Override
    public List<WordList> listWordList(String userId) {
        return wordListRepository.findAllByOwnerUserIdOrderByName(userId);
    }

    @Override
    public List<Word> listWord(String userId, String wordListId) {
        var wordList = wordListRepository.findByIdAndOwnerUserId(wordListId, userId);
        if (wordList == null) {
            throw new ResponseStatusException(NOT_FOUND, "Word list does not exist");
        }

        return StreamSupport.stream(wordRepository.findAllById(wordList.getWordIds()).spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Word> randomWord(String userId, RandomWordListForm form) {
        Set<String> wordIds = new HashSet<>();
        form.getParameters().forEach(parameter -> {
            var wordList = wordListRepository.findByIdAndOwnerUserId(parameter.getWordListId(), userId);
            if (wordList != null) {
                if (parameter.getAnyScoreCount() == null) {
                    // Add all
                    wordIds.addAll(wordList.getWordIds());
                } else {
                    // Add count
                    Set<String> bucketWordIds = new HashSet<>(wordList.getWordIds());
                    bucketWordIds.removeAll(wordIds);
                    if (bucketWordIds.size() <= parameter.getAnyScoreCount()) {
                        // Add the full bucket
                        wordIds.addAll(bucketWordIds);
                    } else {
                        // Shuffle and add desired amount
                        List<String> bucketWordIdsList = new ArrayList<>(bucketWordIds);
                        Collections.shuffle(bucketWordIdsList);
                        bucketWordIdsList.stream().limit(parameter.getAnyScoreCount()).forEach(wordIds::add);
                    }
                }
            }
        });
        return StreamSupport.stream(wordRepository.findAllById(wordIds).spliterator(), false)
                .collect(Collectors.toList());
    }

    private static final Set<Character> separators = new HashSet<>(Arrays.asList('\n', '\r', '\t', '.', ',', '/', ' '));

    static protected List<String> tokenize(String wordsInText) {

        Set<String> words = new LinkedHashSet<>();

        StringBuilder nextWord = new StringBuilder();
        for (int i = 0; i < wordsInText.length(); ++i) {

            // Next word
            var ch = wordsInText.charAt(i);
            if (separators.contains(ch)) {
                if (!nextWord.isEmpty()) {
                    words.add(nextWord.toString());
                }
                nextWord = new StringBuilder();
            } else {
                nextWord.append(ch);
            }

        }

        // Last word
        if (!nextWord.isEmpty()) {
            words.add(nextWord.toString());
        }

        return new ArrayList<>(words);
    }
}
