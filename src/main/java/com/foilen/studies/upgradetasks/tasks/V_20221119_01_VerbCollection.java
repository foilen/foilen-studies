package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import org.springframework.stereotype.Component;

@Component
public class V_20221119_01_VerbCollection extends AbstractMongoUpgradeTask {

    @Override
    public void execute() {
        addCollection("verb");
        addIndex("verb",
                new Tuple2<>("ownerUserId", 1),
                new Tuple2<>("name", 1));
    }

}
