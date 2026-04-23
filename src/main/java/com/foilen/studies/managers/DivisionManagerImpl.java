package com.foilen.studies.managers;

import com.foilen.smalltools.restapi.model.FormResult;
import com.foilen.studies.controllers.models.DivisionScoresSingleResult;
import com.foilen.studies.controllers.models.RandomDivisionForm;
import com.foilen.studies.controllers.models.RandomDivisionResult;
import com.foilen.studies.controllers.models.TrackDivisionForm;
import com.foilen.studies.data.DivisionScoresRepository;
import com.foilen.studies.data.vocabulary.DivisionScores;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class DivisionManagerImpl extends AbstractManager implements DivisionManager {

    @Autowired
    private DivisionScoresRepository divisionScoresRepository;

    @Override
    public RandomDivisionResult random(String userId, RandomDivisionForm form) {

        var result = new RandomDivisionResult();

        // Validate
        validateMin(result, "divisorMax", form.getDivisorMax(), 1);
        validateMax(result, "divisorMax", form.getDivisorMax(), 12);
        validateMin(result, "quotientMax", form.getQuotientMax(), 1);
        validateMax(result, "quotientMax", form.getQuotientMax(), 12);

        if (!result.isSuccess()) {
            return result;
        }

        // Move the smaller one to divisor
        if (form.getQuotientMax() < form.getDivisorMax()) {
            short temp = form.getDivisorMax();
            form.setDivisorMax(form.getQuotientMax());
            form.setQuotientMax(temp);
        }

        // Get or create the scores
        var scores = divisionScoresRepository.findById(userId)
                .orElseGet(() -> new DivisionScores()
                        .setUserId(userId));

        // Generate the questions
        List<DivisionPossibility> possibilities = new ArrayList<>();
        for (short divisor = 1; divisor <= form.getDivisorMax(); ++divisor) {
            for (short quotient = 1; quotient <= form.getQuotientMax(); ++quotient) {
                if (!form.isDivisorAlwaysSmaller() || divisor <= quotient) {
                    possibilities.add(new DivisionPossibility(divisor, quotient, scores.getScores()[divisor - 1][quotient - 1]));
                }
            }
        }
        if (!form.isDivisorAlwaysSmaller()) {
            for (short divisor = (short) (form.getDivisorMax() + 1); divisor <= form.getQuotientMax(); ++divisor) {
                for (short quotient = 1; quotient <= form.getDivisorMax(); ++quotient) {
                    possibilities.add(new DivisionPossibility(divisor, quotient, scores.getScores()[divisor - 1][quotient - 1]));
                }
            }
        }
        logger.info("Generated {} possibilities for {}", possibilities.size(), form);

        Map<ScoreState, List<DivisionPossibility>> possibilitiesByScore = new HashMap<>();
        for (ScoreState scoreState : ScoreState.values()) {
            possibilitiesByScore.put(scoreState, new ArrayList<>());
        }

        // Store by score
        for (DivisionPossibility possibility : possibilities) {
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
        for (List<DivisionPossibility> list : possibilitiesByScore.values()) {
            Collections.shuffle(list);
        }

        int amountLeft = form.getAmount();
        List<Short[]> questions = new ArrayList<>();

        // Add all the not done
        for (DivisionPossibility possibility : possibilitiesByScore.get(ScoreState.NA)) {
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.divisor(), possibility.quotient()});
            --amountLeft;
        }

        // Add 50% of 0/1
        int max = amountLeft == 0 ? 0 : Math.max(1, amountLeft / 2);
        logger.info("Adding 50% of RED ; max={}", max);
        for (DivisionPossibility possibility : possibilitiesByScore.get(ScoreState.RED)) {
            if (max == 0) {
                break;
            }
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.divisor(), possibility.quotient()});
            --amountLeft;
            --max;
        }

        // Add 50% of 2/3
        max = amountLeft == 0 ? 0 : Math.max(1, amountLeft / 2);
        logger.info("Adding 50% of YELLOW ; max={}", max);
        for (DivisionPossibility possibility : possibilitiesByScore.get(ScoreState.YELLOW)) {
            if (max == 0) {
                break;
            }
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.divisor(), possibility.quotient()});
            --amountLeft;
            --max;
        }

        // Add the 4/5
        for (DivisionPossibility possibility : possibilitiesByScore.get(ScoreState.GREEN)) {
            if (amountLeft == 0) {
                break;
            }
            questions.add(new Short[]{possibility.divisor(), possibility.quotient()});
            --amountLeft;
        }

        // Add randomly
        logger.info("Adding randomly ; amountLeft={}", amountLeft);
        while (amountLeft > 0) {
            int index = (int) (Math.random() * possibilities.size());
            DivisionPossibility possibility = possibilities.get(index);
            questions.add(new Short[]{possibility.divisor(), possibility.quotient()});
            --amountLeft;
        }

        // Randomize the questions
        Collections.shuffle(questions);

        // Return the result
        result.setQuestions(questions);
        return result;

    }

    @Override
    public DivisionScoresSingleResult scores(String userId) {

        // Get or create the scores
        var scores = divisionScoresRepository.findById(userId)
                .orElseGet(() -> new DivisionScores()
                        .setUserId(userId));

        var result = new DivisionScoresSingleResult();
        result.setItem(scores);
        return result;

    }

    @Override
    public FormResult track(String userId, TrackDivisionForm form) {

        var result = new FormResult();

        // Validate
        validateMin(result, "divisor", form.getDivisor(), 1);
        validateMax(result, "divisor", form.getDivisor(), 12);
        validateMin(result, "quotient", form.getQuotient(), 1);
        validateMax(result, "quotient", form.getQuotient(), 12);

        if (!result.isSuccess()) {
            return result;
        }

        // Get or create the scores
        var scores = divisionScoresRepository.findById(userId)
                .orElseGet(() -> new DivisionScores()
                        .setUserId(userId));

        // Update the score
        Short score = scores.getScores()[form.getDivisor() - 1][form.getQuotient() - 1];
        if (score == null) {
            score = (short) (form.isSuccess() ? 4 : 3);
        } else {
            score = (short) (form.isSuccess() ? Math.min(score + 1, 5) : Math.max(score - 1, 0));
        }
        scores.getScores()[form.getDivisor() - 1][form.getQuotient() - 1] = score;

        // Save
        divisionScoresRepository.save(scores);

        return new FormResult();

    }
}
