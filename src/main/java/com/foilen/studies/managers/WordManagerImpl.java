package com.foilen.studies.managers;

import com.foilen.smalltools.listscomparator.ListComparatorHandler;
import com.foilen.smalltools.listscomparator.ListsComparator;
import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.restapi.services.FormValidationTools;
import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.CollectionsTools;
import com.foilen.smalltools.tools.StringTools;
import com.foilen.studies.controllers.models.RandomWordListForm;
import com.foilen.studies.controllers.models.TrackForm;
import com.foilen.studies.controllers.models.WordListExpended;
import com.foilen.studies.data.UserScoresRepository;
import com.foilen.studies.data.WordListRepository;
import com.foilen.studies.data.WordRepository;
import com.foilen.studies.data.vocabulary.*;
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
    private UserScoresRepository userScoresRepository;
    @Autowired
    private WordListRepository wordListRepository;

    @Override
    public List<Word> bulkSplit(String userId, String wordsInText) {

        logger.info("User {} is getting words in bulk", userId);

        var wordNames = tokenize(wordsInText);
        logger.info("Will have {} words", wordNames.size());

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

        return wordByName.values().stream()
                .sorted(Comparator.comparing(Word::getWord))
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

    @Override
    public FormResult track(String userId, TrackForm form) {

        logger.info("Tracking score for user {} : {}", userId, form);

        // Validate
        FormResult formResult = new FormResult();
        FormValidationTools.validateMandatory(formResult, "wordId", form.getWordId());
        if (!formResult.isSuccess()) {
            return formResult;
        }

        // Get the current scores and update them
        var userScores = getOrCreateUserScores(userId);
        var score = userScores.getScoreByWordId().computeIfAbsent(form.getWordId(), wordId -> new Score());
        var lastScoreItems = score.getLastScoreItems();
        lastScoreItems.add(new ScoreItem(form.isSuccess(), form.getAnswer()));

        // Keep only last 10
        while (lastScoreItems.size() > 10) {
            lastScoreItems.remove(0);
        }

        // Compute score
        int successes = (int) lastScoreItems.stream().filter(ScoreItem::isSuccess).count();
        int total = lastScoreItems.size();
        int percent = 100 * successes / total;
        if (percent > 66) { // Good
            score.setScore(3);
        } else if (percent > 33) { // Average
            score.setScore(2);
        } else { // Bad
            score.setScore(1);
        }
        userScoresRepository.save(userScores);
        return formResult;
    }

    @Override
    public List<WordList> listWordList(String userId) {
        return wordListRepository.findAllByOwnerUserIdOrderByName(userId);
    }

    @Override
    public FormResult saveWordList(String userId, WordListExpended form) {

        // Validation
        FormResult formResult = new FormResult();
        FormValidationTools.validateMandatory(formResult, "name", form.getName());

        if (!formResult.isSuccess()) {
            return formResult;
        }

        // Get the words
        var desiredWords = form.getWords();
        var desiredIds = desiredWords.stream().map(Word::getId).collect(Collectors.toSet());
        var existingWords = wordRepository.findAllByOwnerUserIdAndIdIn(userId, desiredIds);
        var existingWordIds = existingWords.stream().map(Word::getId).collect(Collectors.toSet());
        if (existingWords.size() != desiredWords.size()) {
            desiredIds.removeAll(existingWordIds);
            desiredIds.forEach(missingId -> CollectionsTools.getOrCreateEmptyArrayList(formResult.getValidationErrorsByField(), "word." + missingId, String.class).add("Not found"));
            return formResult;
        }

        // Get or create
        var wordList = wordListRepository.findByIdAndOwnerUserId(form.getId(), userId);
        if (wordList == null) {
            wordList = new WordList();
            wordList.setOwnerUserId(userId);
        }
        wordList.setName(form.getName());
        wordList.setWordIds(new ArrayList<>(existingWordIds));

        // Update the speech if changed
        ListsComparator.compareStreams(
                existingWords.stream().sorted(Comparator.comparing(Word::getId)),
                desiredWords.stream().sorted(Comparator.comparing(Word::getId)),
                (a, b) -> a.getId().compareTo(b.getId()),
                new ListComparatorHandler<>() {
                    @Override
                    public void both(Word existing, Word desired) {
                        if (!StringTools.safeEquals(existing.getSpeakText().getText(), desired.getSpeakText().getText())) {
                            existing.getSpeakText().setText(desired.getSpeakText().getText());
                            existing.getSpeakText().computeCacheId();
                            wordRepository.save(existing);
                        }
                    }

                    @Override
                    public void leftOnly(Word existing) {
                    }

                    @Override
                    public void rightOnly(Word desired) {
                    }
                }
        );

        wordListRepository.save(wordList);
        return formResult;
    }

    @Override
    public FormResult deleteWordList(String userId, String wordListId) {
        var wordList = wordListRepository.findByIdAndOwnerUserId(wordListId, userId);
        FormResult result = new FormResult();
        if (wordList == null) {
            result.getGlobalErrors().add("Word list does not exist");
            return result;
        }

        logger.info("Deleting word list {} for user {}", wordListId, userId);
        wordListRepository.delete(wordList);

        return result;
    }

    @Override
    public WordListExpended getWordListExpended(String userId, String wordListId) {
        var wordList = wordListRepository.findByIdAndOwnerUserId(wordListId, userId);
        if (wordList == null) {
            throw new ResponseStatusException(NOT_FOUND, "Word list does not exist");
        }

        var response = new WordListExpended();
        response.setId(wordList.getId());
        response.setName(wordList.getName());
        response.setOwnerUserId(wordList.getOwnerUserId());
        response.setWords(StreamSupport.stream(wordRepository.findAllById(wordList.getWordIds()).spliterator(), false)
                .collect(Collectors.toList()));

        return response;
    }

    private UserScores getOrCreateUserScores(String userId) {
        var userScores = userScoresRepository.findById(userId).orElse(null);
        if (userScores == null) {
            userScores = new UserScores();
            userScores.setUserId(userId);
            userScores = userScoresRepository.save(userScores);
        }
        return userScores;
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
                    words.add(nextWord.toString().toLowerCase());
                }
                nextWord = new StringBuilder();
            } else {
                nextWord.append(ch);
            }

        }

        // Last word
        if (!nextWord.isEmpty()) {
            words.add(nextWord.toString().toLowerCase());
        }

        return new ArrayList<>(words);
    }
}
