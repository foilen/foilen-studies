package com.foilen.studies.data;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.studies.data.vocabulary.Word;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static org.springframework.data.mongodb.core.query.Criteria.expr;

@Service
public class WordRepositoryImpl extends AbstractBasics implements WordRepositoryCustom {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public Stream<Word> findAllWithSpeakTextSameAsWord() {
        return mongoOperations.query(Word.class)
                .matching(expr(MongoExpression.create("{ $eq: [\"$word\", \"$speakText.text\"] }")))
                .stream();
    }

}
