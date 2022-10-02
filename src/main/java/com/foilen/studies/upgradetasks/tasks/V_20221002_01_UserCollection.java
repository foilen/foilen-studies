package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import org.springframework.stereotype.Component;

@Component
public class V_20221002_01_UserCollection extends AbstractMongoUpgradeTask {

    @Override
    public void execute() {
        addCollection("userDetails");
        addIndex("userDetails", new Tuple2<>("providerIds", 1));
    }

}
