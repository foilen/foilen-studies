package com.foilen.studies.upgradetasks.tasks;

import com.foilen.smalltools.upgrader.trackers.AbstractMongoUpgradeTask;
import org.bson.BsonDocument;
import org.springframework.stereotype.Component;

/**
 * Delete all sessions from MongoDB to resolve serialization issues after library upgrades.
 * This fixes InvalidClassException due to serialVersionUID mismatch in OidcUserAuthority.
 */
@Component
public class V_20251022_01_DeleteAllSessions extends AbstractMongoUpgradeTask {

    @Override
    public void execute() {
        // Delete all documents from the sessions collection
        mongoClient.getDatabase(databaseName).getCollection("sessions").deleteMany(new BsonDocument());
    }

}
