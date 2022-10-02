package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import org.springframework.stereotype.Component;

@Component
public class V_20221003_01_WordCollection extends AbstractMongoUpgradeTask {

    @Override
    public void execute() {
        addCollection("word");
        addCollection("wordList");

        addIndex("word",
                new Tuple2<>("ownerUserId", 1),
                new Tuple2<>("word", 1));
        addIndex("wordList",
                new Tuple2<>("ownerUserId", 1),
                new Tuple2<>("name", 1));
    }

}
