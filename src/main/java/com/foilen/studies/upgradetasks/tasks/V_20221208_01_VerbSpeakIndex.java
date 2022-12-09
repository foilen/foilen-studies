package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import org.springframework.stereotype.Component;

@Component
public class V_20221208_01_VerbSpeakIndex extends AbstractMongoUpgradeTask {

    @Override
    public void execute() {
        addIndex("verb",
                new Tuple2<>("speakText.verbLines.speakText.cacheId", 1));
    }

}
