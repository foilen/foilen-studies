package com.foilen.studies.data;

import com.foilen.studies.data.vocabulary.Word;

import java.util.stream.Stream;

public interface WordRepositoryCustom {

    Stream<Word> findAllWithSpeakTextSameAsWord();

}
