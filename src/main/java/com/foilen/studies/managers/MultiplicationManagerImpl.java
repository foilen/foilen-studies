package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.MultiplicationScoresSingleResult;
import com.foilen.studies.controllers.models.RandomMultiplicationForm;
import com.foilen.studies.controllers.models.RandomMultiplicationResult;
import com.foilen.studies.controllers.models.TrackMultiplicationForm;
import com.foilen.studies.data.MultiplicationScoresRepository;
import com.foilen.studies.data.vocabulary.MultiplicationScores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class MultiplicationManagerImpl extends AbstractManager implements MultiplicationManager {

    @Autowired
    private MultiplicationScoresRepository multiplicationScoresRepository;

    @Override
    public RandomMultiplicationResult random(String userId, RandomMultiplicationForm form) {

        var result = new RandomMultiplicationResult();

        // Validate
        validateMin(result, "leftMax", form.getLeftMax(), 1);
        validateMax(result, "leftMax", form.getLeftMax(), 12);
        validateMin(result, "rightMax", form.getRightMax(), 1);
        validateMax(result, "rightMax", form.getRightMax(), 12);

        if (!result.isSuccess()) {
            return result;
        }

        // Get or create the scores
        var scores = multiplicationScoresRepository.findById(userId)
                .orElseGet(() -> new MultiplicationScores()
                        .setUserId(userId));

        // Generate the questions
        List<MultiplicationPossibility> possibilities = new ArrayList<>();
        for (short left = 1; left <= form.getLeftMax(); ++left) {
            for (short right = 1; right <= form.getRightMax(); ++right) {
                if (!form.isLeftAlwaysSmaller() || left <= right) {
                    possibilities.add(new MultiplicationPossibility(left, right, scores.getScores()[left - 1][right - 1]));
                }
            }
        }
        logger.info("Generated {} possibilities for {}", possibilities.size(), form);

        Map<ScoreState, List<MultiplicationPossibility>> possibilitiesByScore = new HashMap<>();
        for (ScoreState scoreState : ScoreState.values()) {
            possibilitiesByScore.put(scoreState, new ArrayList<>());
        }

        // Store by score
        for (MultiplicationPossibility possibility : possibilities) {
            if (possibility.score() == null) {
                possibilitiesByScore.get(ScoreState.NA).add(possibility);
                continue;
            }
            switch (possibility.score()) {
                case 0, 1 -> possibilitiesByScore.get(ScoreState.RED).add(possibility);
                case 2, 3 -> possibilitiesByScore.get(ScoreState.YELLOW).add(possibility);
                case 4, 5 -> possibilitiesByScore.get(ScoreState.GREEN).add(possibility);
            }
        }

        // Shuffle the lists
        for (List<MultiplicationPossibility> list : possibilitiesByScore.values()) {
            Collections.shuffle(list);
        }

        int amountLeft = form.getAmount();
        List<Short[]> questions = new ArrayList<>();

        // Add all the not done
        for (MultiplicationPossibility possibility : possibilitiesByScore.get(ScoreState.NA)) {
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.left(), possibility.right()});
            --amountLeft;
        }

        // Add 50% of 0/1
        int max = amountLeft == 0 ? 0 : Math.max(1, amountLeft / 2);
        logger.info("Adding 50% of RED ; max={}", max);
        for (MultiplicationPossibility possibility : possibilitiesByScore.get(ScoreState.RED)) {
            if (max == 0) {
                break;
            }
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.left(), possibility.right()});
            --amountLeft;
            --max;
        }

        // Add 50% of 2/3
        max = amountLeft == 0 ? 0 : Math.max(1, amountLeft / 2);
        logger.info("Adding 50% of YELLOW ; max={}", max);
        for (MultiplicationPossibility possibility : possibilitiesByScore.get(ScoreState.YELLOW)) {
            if (max == 0) {
                break;
            }
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.left(), possibility.right()});
            --amountLeft;
            --max;
        }

        // Add the 4/5
        for (MultiplicationPossibility possibility : possibilitiesByScore.get(ScoreState.GREEN)) {
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.left(), possibility.right()});
            --amountLeft;
        }

        // Add randomly
        logger.info("Adding randomly ; amountLeft={}", amountLeft);
        while (amountLeft > 0) {
            int index = (int) (Math.random() * possibilities.size());
            MultiplicationPossibility possibility = possibilities.get(index);
            questions.add(new Short[]{possibility.left(), possibility.right()});
            --amountLeft;
        }

        // Randomize the questions
        Collections.shuffle(questions);

        // Return the result
        result.setQuestions(questions);
        return result;

    }

    @Override
    public MultiplicationScoresSingleResult scores(String userId) {

        // Get or create the scores
        var scores = multiplicationScoresRepository.findById(userId)
                .orElseGet(() -> new MultiplicationScores()
                        .setUserId(userId));

        var result = new MultiplicationScoresSingleResult();
        result.setItem(scores);
        return result;

    }

    @Override
    public FormResult track(String userId, TrackMultiplicationForm form) {

        var result = new FormResult();

        // Validate
        validateMin(result, "left", form.getLeft(), 1);
        validateMax(result, "left", form.getLeft(), 12);
        validateMin(result, "right", form.getRight(), 1);
        validateMax(result, "right", form.getRight(), 12);

        if (!result.isSuccess()) {
            return result;
        }

        // Get or create the scores
        var scores = multiplicationScoresRepository.findById(userId)
                .orElseGet(() -> new MultiplicationScores()
                        .setUserId(userId));

        // Update the score
        Short score = scores.getScores()[form.getLeft() - 1][form.getRight() - 1];
        if (score == null) {
            score = (short) (form.isSuccess() ? 4 : 3);
        } else {
            score = (short) (form.isSuccess() ? Math.min(score + 1, 5) : Math.max(score - 1, 0));
        }
        scores.getScores()[form.getLeft() - 1][form.getRight() - 1] = score;

        // Save
        multiplicationScoresRepository.save(scores);

        return new FormResult();

    }
}
