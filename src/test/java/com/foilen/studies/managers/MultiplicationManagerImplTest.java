package com.foilen.studies.managers;

import com.foilen.studies.controllers.models.RandomMultiplicationForm;
import com.foilen.studies.controllers.models.RandomMultiplicationResult;
import com.foilen.studies.data.MultiplicationScoresRepositoryEmptyMock;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MultiplicationManagerImplTest {

    @Test
    void random_leftAlwaysSmaller() {
        // Prepare
        var manager = new MultiplicationManagerImpl();
        ReflectionTestUtils.setField(manager, "multiplicationScoresRepository", new MultiplicationScoresRepositoryEmptyMock());
        var form = new RandomMultiplicationForm()
                .setLeftMax((short) 5)
                .setRightMax((short) 10)
                .setAmount((short) 200)
                .setLeftAlwaysSmaller(true);

        // Execute
        var result = manager.random("user1", form);

        // Validate
        assertTrue(result.isSuccess());
        var questions = getQuestionsWithoutDuplicates(result);

        assertEquals(40, questions.size());
        for (var question : questions) {
            assertTrue(question[0] <= question[1]);
        }

    }

    @Test
    void random_leftAlwaysSmaller_reverse() {
        // Prepare
        var manager = new MultiplicationManagerImpl();
        ReflectionTestUtils.setField(manager, "multiplicationScoresRepository", new MultiplicationScoresRepositoryEmptyMock());
        var form = new RandomMultiplicationForm()
                .setLeftMax((short) 10)
                .setRightMax((short) 5)
                .setAmount((short) 200)
                .setLeftAlwaysSmaller(true);

        // Execute
        var result = manager.random("user1", form);

        assertTrue(result.isSuccess());
        var questions = getQuestionsWithoutDuplicates(result);

        assertEquals(40, questions.size());
        for (var question : questions) {
            assertTrue(question[0] <= question[1]);
        }

    }

    @Test
    void random_all() {
        // Prepare
        var manager = new MultiplicationManagerImpl();
        ReflectionTestUtils.setField(manager, "multiplicationScoresRepository", new MultiplicationScoresRepositoryEmptyMock());
        var form = new RandomMultiplicationForm()
                .setLeftMax((short) 5)
                .setRightMax((short) 10)
                .setAmount((short) 200)
                .setLeftAlwaysSmaller(false);

        // Execute
        var result = manager.random("user1", form);

        // Validate
        assertTrue(result.isSuccess());
        var questions = getQuestionsWithoutDuplicates(result);

        assertEquals(75, questions.size());
    }

    @Test
    void random_all_reverse() {
        // Prepare
        var manager = new MultiplicationManagerImpl();
        ReflectionTestUtils.setField(manager, "multiplicationScoresRepository", new MultiplicationScoresRepositoryEmptyMock());
        var form = new RandomMultiplicationForm()
                .setLeftMax((short) 10)
                .setRightMax((short) 5)
                .setAmount((short) 200)
                .setLeftAlwaysSmaller(false);

        // Execute
        var result = manager.random("user1", form);

        // Validate
        assertTrue(result.isSuccess());
        var questions = getQuestionsWithoutDuplicates(result);

        assertEquals(75, questions.size());
    }

    private static List<Short[]> getQuestionsWithoutDuplicates(RandomMultiplicationResult result) {
        var questions = result.getQuestions();
        // Remove duplicates
        for (int i = 0; i < questions.size(); ++i) {
            for (int j = i + 1; j < questions.size(); ++j) {
                if (questions.get(i)[0] == questions.get(j)[0] && questions.get(i)[1] == questions.get(j)[1]) {
                    questions.remove(j);
                    --j;
                }
            }
        }
        return questions;
    }

}