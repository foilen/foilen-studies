package com.foilen.studies.managers;

import com.foilen.smalltools.test.asserts.AssertTools;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class WordManagerImplTest {

    @Test
    void tokenizeWithSpaceAsSeparator() {
        var wordsInText = """
                first,second, third
                fourth/fifth. sixth
                                
                super-high,fifth,FIFTH,École
                aujourd'hui, aujourd`hui
                hard* (abc)def/ghi\\jkl|mno+pqr;stu!vwx?yz
                """;
        AssertTools.assertJsonComparison(
                Arrays.asList("first", "second", "third",
                        "fourth", "fifth", "sixth",
                        "super-high", "école",
                        "aujourd'hui",
                        "hard", "abc", "def", "ghi", "jkl", "mno", "pqr", "stu", "vwx", "yz"),
                WordManagerImpl.tokenize(wordsInText, false)
        );
    }

    @Test
    void tokenizeAcceptSpacesInWords() {
        var wordsInText = """
                froide, froid
                parce que, joie
                """;
        AssertTools.assertJsonComparison(
                Arrays.asList("froide", "froid",
                        "parce que", "joie"),
                WordManagerImpl.tokenize(wordsInText, true)
        );
    }
}