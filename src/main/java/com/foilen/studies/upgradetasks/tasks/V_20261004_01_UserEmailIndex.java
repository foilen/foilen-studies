package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.tuple.Tuple2;
import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import org.springframework.stereotype.Component;

/**
 * Users now log in with their email. Existing users (that came from the OAuth2 providers) have no email: set it manually on the "userDetails" document to link an account.
 */
@Component
public class V_20261004_01_UserEmailIndex extends AbstractMongoUpgradeTask {

    @Override
    public void execute() {
        addIndex("userDetails", new Tuple2<>("email", 1));
    }

}
