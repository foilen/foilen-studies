package com.foilen.studies.fakedata;

import com.foilen.smalltools.tools.AbstractBasics;
import com.foilen.smalltools.tools.ResourceTools;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FakeDataLoader extends AbstractBasics {

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    public void loadFakeData() {

        var collectionNames = new String[]{
                "speakTextCacheFile",
                "userDetails",
                "userScores",
                "word",
                "wordList",
        };

        MongoDatabase database = mongoClient.getDatabase(databaseName);
        for (var collectionName : collectionNames) {
            logger.info("Deleting documents in collection {} and loading data", collectionName);
            MongoCollection<Document> collection = database.getCollection(collectionName);
            collection.deleteMany(Filters.empty());

            ResourceTools.readResourceLinesIteration(collectionName + ".json", getClass())
                    .forEach(line -> {
                        Document document = Document.parse(line);
                        collection.insertOne(document);
                    });

            logger.info("Loaded {} documents in collection {}", collection.countDocuments(), collectionName);
        }

    }
}
