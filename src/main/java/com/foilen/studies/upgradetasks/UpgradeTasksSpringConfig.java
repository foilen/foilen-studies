package com.foilen.studies.upgradetasks;

import com.foilen.smalltools.upgrader.UpgraderTools;
import com.foilen.smalltools.upgrader.tasks.UpgradeTask;
import com.foilen.smalltools.upgrader.trackers.MongoDbUpgraderTracker;
import com.foilen.smalltools.upgrader.trackers.UpgraderTracker;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UpgradeTasksSpringConfig {

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public UpgraderTools upgraderTools(List<UpgradeTask> tasks) {
        var upgraderTools = new UpgraderTools(tasks);
        upgraderTools.setDefaultUpgraderTracker(mongodbUpgraderTracker());
        upgraderTools.getUpgraderTrackerByName().put("mongodb", mongodbUpgraderTracker());
        return upgraderTools;
    }

    @Bean
    public UpgraderTracker mongodbUpgraderTracker() {
        return new MongoDbUpgraderTracker(mongoClient, databaseName);
    }

}
