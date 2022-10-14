package com.foilen.studies.managers;

import com.foilen.smalltools.test.asserts.AssertTools;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class WordManagerImplTest {

    @Test
    void tokenize() {
        var wordsInText = """
                first,second, third
                fourth/fifth. sixth
                                
                super-high,fifth,FIFTH,École
                """;
        AssertTools.assertJsonComparison(
                Arrays.asList("first", "second", "third", "fourth", "fifth", "sixth", "super-high", "école"),
                WordManagerImpl.tokenize(wordsInText)
        );
    }
}