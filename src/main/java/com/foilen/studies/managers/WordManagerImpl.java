package com.foilen.studies.managers;

import com.foilen.smalltools.listscomparator.ListComparatorHandler;
import com.foilen.smalltools.listscomparator.ListsComparator;
import com.foilen.smalltools.mongodb.distributed.MongoDbDeque;
import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.smalltools.restapi.services.FormValidationTools;
import com.foilen.smalltools.tools.*;
import com.foilen.studies.controllers.models.*;
import com.foilen.studies.data.UserScoresRepository;
import com.foilen.studies.data.WordListRepository;
import com.foilen.studies.data.WordRepository;
import com.foilen.studies.data.vocabulary.*;
import com.foilen.studies.services.AiGenerationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@Transactional
public class WordManagerImpl extends AbstractBasics implements WordManager {

    private static final String CACHE_CANNOT_GEN_SENTENCE = "cannotGenSentence";

    @Autowired
    private AiGenerationService aiGenerationService;
    @Autowired
    private CacheManager cacheManager;
    private Cache cannotGenSentenceCache;
    @Autowired
    private Environment environment;
    @Autowired
    private MongoDbDeque<String> generateSentenceForWordsWithoutOneQueue;
    @Autowired
    private UserScoresRepository userScoresRepository;
    @Autowired
    private WordListRepository wordListRepository;
    @Autowired
    private WordRepository wordRepository;

    @PostConstruct
    public void initProcessingSentenceGeneration() {

        // Get the cache
        cannotGenSentenceCache = cacheManager.getCache(CACHE_CANNOT_GEN_SENTENCE);

        // Check the active profiles and must not contain "test"
        if (Arrays.stream(environment.getActiveProfiles()).anyMatch("test"::equals)) {
            logger.info("Skipping initProcessingSentenceGeneration because of test profile");
            return;
        }

        ExecutorsTools.getCachedDaemonThreadPool().execute(() -> {

            ThreadTools.nameThread()
                    .clear().setSeparator("-")
                    .appendText("processingSentenceGeneration")
                    .change();

            // Add some words to generate sentences for if none
            generateSentenceForWordsWithoutOneAddMoreIfNoneOnTheQueue();

            while (true) {

                try {

                    // Get the next word
                    logger.info("Waiting for a word to generate a sentence for");
                    String wordId = generateSentenceForWordsWithoutOneQueue.take();
                    while ((wordId != null)) {
                        // Get the word with the id
                        logger.info("Getting word {}", wordId);
                        var word = wordRepository.findById(wordId).orElse(null);
                        if (word == null) {
                            logger.error("Word {} not found", wordId);
                        } else {

                            // Check if the word is still the same as the text
                            if (StringTools.safeEquals(word.getWord(), word.getSpeakText().getText())) {
                                try {
                                    logger.info("Generating sentence for word {}", word.getWord());
                                    var sentence = aiGenerationService.generateSentence(getLocale(word), word.getWord());
                                    logger.info("Generated sentence for word {}: {}", word.getWord(), sentence);
                                    word.getSpeakText().setText(word.getWord() + ". " + sentence);
                                    word.getSpeakText().computeCacheId();
                                    wordRepository.save(word);
                                } catch (Exception e) {
                                    cannotGenSentenceCache.put(word.getWord(), true);
                                    logger.error("Could not generate sentence for word {}", word.getWord(), e);
                                }
                            }

                        }

                        // Get the next word if any available in the next 15 seconds
                        wordId = generateSentenceForWordsWithoutOneQueue.poll(15, TimeUnit.SECONDS);
                        if (wordId == null) {
                            logger.info("No word in the queue. Will try to add more and wait");
                            generateSentenceForWordsWithoutOne();
                        }

                    }

                } catch (Exception e) {
                    logger.error("Unexpected issue. Sleeping 1 minute", e);
                    ThreadTools.sleep(60000);
                }
            }

        });
    }

    @Override
    public List<Word> bulkSplit(String userId, String wordsInText, boolean acceptSpacesInWords) {

        logger.info("User {} is getting words in bulk", userId);

        var wordNames = tokenize(wordsInText, acceptSpacesInWords);
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

        var scoreByWordId = getOrCreateUserScores(userId).getScoreByWordId();

        Set<String> selectedWordIds = new HashSet<>();
        form.getParameters().forEach(parameter -> {
            Collection<String> wordListIds;
            String wordListId = parameter.getWordListId();

            // Select from any list
            if (wordListId == null) {
                wordListIds = wordListRepository.findAllByOwnerUserId(userId).stream() //
                        .flatMap(wordList -> wordList.getWordIds().stream()) //
                        .collect(Collectors.toSet());
            } else {
                // Specific list
                var wordList = wordListRepository.findByIdAndOwnerUserId(wordListId, userId);
                if (wordList == null) {
                    return;
                } else {
                    wordListIds = wordList.getWordIds();
                }
            }

            randomWordAddSomeIds(selectedWordIds, wordListIds, scoreByWordId, -1, parameter.getAnyScoreCount());
            randomWordAddSomeIds(selectedWordIds, wordListIds, scoreByWordId, 0, parameter.getNoScoreCount());
            randomWordAddSomeIds(selectedWordIds, wordListIds, scoreByWordId, 1, parameter.getBadScoreCount());
            randomWordAddSomeIds(selectedWordIds, wordListIds, scoreByWordId, 2, parameter.getAverageScoreCount());
            randomWordAddSomeIds(selectedWordIds, wordListIds, scoreByWordId, 3, parameter.getGoodScoreCount());
        });
        return StreamSupport.stream(wordRepository.findAllById(selectedWordIds).spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Select some random words.
     *
     * @param selectedWordIds the list of all the selected words that will be populated
     * @param wordListIds     the word ids to pick words from
     * @param scoreByWordId   the scores for each word
     * @param desiredScore    -1 for any ; 0 for no score ; >=1 as exact score
     * @param amount          the max amount of words to pick
     */
    private void randomWordAddSomeIds(Set<String> selectedWordIds, Collection<String> wordListIds, Map<String, Score> scoreByWordId, int desiredScore, int amount) {
        if (amount == 0) {
            return;
        }
        Set<String> bucketWordIds = wordListIds.stream()
                .filter(wordId -> {
                    // Any
                    if (desiredScore == -1) {
                        return true;
                    }

                    // Specific score
                    var wordScore = scoreByWordId.get(wordId);
                    int currentScore = 0;
                    if (wordScore != null) {
                        currentScore = wordScore.getScore();
                    }
                    return desiredScore == currentScore;
                })
                .collect(Collectors.toSet());
        bucketWordIds.removeAll(selectedWordIds);
        if (bucketWordIds.size() <= amount) {
            // Add the full bucket
            selectedWordIds.addAll(bucketWordIds);
        } else {
            // Shuffle and add desired amount
            List<String> bucketWordIdsList = new ArrayList<>(bucketWordIds);
            Collections.shuffle(bucketWordIdsList);
            bucketWordIdsList.stream().limit(amount).forEach(selectedWordIds::add);
        }
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
    public List<WordListWithScore> listWordList(String userId) {

        var userScores = getOrCreateUserScores(userId);

        return wordListRepository.findAllByOwnerUserIdOrderByName(userId).stream()
                .map(wordList -> {
                    var item = JsonTools.clone(wordList, WordListWithScore.class);
                    var scoreByWordId = userScores.getScoreByWordId();

                    item.getWordIds().forEach(wordId -> {
                        var score = scoreByWordId.get(wordId);
                        Scores scores = item.getScores();
                        if (score == null) {
                            scores.addNoScore();
                        } else {
                            switch (score.getScore()) {
                                case 1:
                                    scores.addBad();
                                    break;
                                case 2:
                                    scores.addAverage();
                                    break;
                                case 3:
                                    scores.addGood();
                                    break;
                            }
                        }
                    });

                    return item;
                })
                .collect(Collectors.toList());
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
        var desiredWords = form.getWords().stream().map(it -> JsonTools.clone(it, Word.class)).collect(Collectors.toSet());
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
        generateSentenceForWordsWithoutOneAddMoreIfNoneOnTheQueue();
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

        var userScoreByWordId = getOrCreateUserScores(userId).getScoreByWordId();

        var response = new WordListExpended();
        response.setId(wordList.getId());
        response.setName(wordList.getName());
        response.setOwnerUserId(wordList.getOwnerUserId());
        response.setWords(StreamSupport.stream(wordRepository.findAllById(wordList.getWordIds()).spliterator(), false)
                .map(word -> {
                    var wordWithScore = JsonTools.clone(word, WordWithScore.class);
                    var score = userScoreByWordId.get(word.getId());
                    if (score != null) {
                        wordWithScore.setScore(score.getScore());
                    }
                    return wordWithScore;
                })
                .collect(Collectors.toList()));

        return response;
    }

    @Override
    public void generateSentenceForWordsWithoutOne() {
        logger.info("Generating sentences for words without one");
        wordRepository.findAllWithSpeakTextSameAsWord().forEach(word -> {
            // Check if in the cache that cannot be generated
            if (cannotGenSentenceCache.get(word.getWord()) != null) {
                logger.info("Skipping word {} because it cannot be generated", word.getWord());
                return;
            }

            logger.info("Adding word {} to the queue", word.getWord());
            generateSentenceForWordsWithoutOneQueue.add(word.getId());
        });
    }

    @Override
    public void generateSentenceForWordsWithoutOneAddMoreIfNoneOnTheQueue() {
        logger.info("Adding more words to generate sentences for if none");
        if (generateSentenceForWordsWithoutOneQueue.isEmpty()) {
            logger.info("Queue is empty. Adding more words");
            generateSentenceForWordsWithoutOne();
        } else {
            logger.info("Queue is not empty. Will not add more");
        }
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

    private static final Set<Character> SEPARATORS = new HashSet<>(Arrays.asList('\n', '\r', '\t', '.', ',', '/', '+', '*', '\\', '|', ';', '!', '?', '(', ')'));
    private static final Set<Character> SEPARATORS_WITH_SPACE = new HashSet<>(Arrays.asList('\n', '\r', '\t', '.', ',', '/', ' ', '+', '*', '\\', '|', ';', '!', '?', '(', ')'));
    private static final Map<Character, Character> replacements = Map.of('`', '\'');

    private static Locale getLocale(Word word) {
        return switch (word.getSpeakText().getLanguage()) {
            case EN -> Locale.ENGLISH;
            case FR -> Locale.FRENCH;
        };
    }

    static protected List<String> tokenize(String wordsInText, boolean acceptSpacesInWords) {

        Set<Character> separators = acceptSpacesInWords ? SEPARATORS : SEPARATORS_WITH_SPACE;

        Set<String> words = new LinkedHashSet<>();

        StringBuilder nextWord = new StringBuilder();
        for (int i = 0; i < wordsInText.length(); ++i) {

            // Next word
            var ch = wordsInText.charAt(i);
            if (replacements.containsKey(ch)) {
                ch = replacements.get(ch);
            }

            if (separators.contains(ch)) {
                if (!nextWord.isEmpty()) {
                    words.add(nextWord.toString().toLowerCase().trim());
                }
                nextWord = new StringBuilder();
            } else {
                nextWord.append(ch);
            }

        }

        // Last word
        if (!nextWord.isEmpty()) {
            words.add(nextWord.toString().toLowerCase().trim());
        }

        return new ArrayList<>(words);
    }
}
