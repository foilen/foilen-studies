package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import com.mongodb.client.model.IndexOptions;
import org.springframework.stereotype.Component;

@Component
public class V_20221004_01_SpeakTextCacheFile extends AbstractMongoUpgradeTask {

    @Override
    public void execute() {
        addCollection("speakTextCacheFile");

        addIndex("speakTextCacheFile",
                new IndexOptions().unique(true),
                new Tuple2<>("cacheId", 1));

        addIndex("word",
                new Tuple2<>("speakText.cacheId", 1));
    }

}
