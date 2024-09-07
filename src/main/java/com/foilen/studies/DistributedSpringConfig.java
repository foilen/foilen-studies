package com.foilen.studies;

import com.foilen.smalltools.mongodb.distributed.MongoDbDeque;
import com.foilen.smalltools.mongodb.distributed.MongoDbReentrantLock;
import com.foilen.smalltools.mongodb.spring.cache.MongoDbCacheManager;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DistributedSpringConfig {

    @Autowired
    private MongoClient mongoClient;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Bean
    public MongoDbReentrantLock mongoDbReentrantLock() {
        var collection = mongoClient.getDatabase(databaseName).getCollection("lock");
        return new MongoDbReentrantLock(mongoClient, collection);
    }

    @Bean
    public MongoDbCacheManager cacheManager() {
        return new MongoDbCacheManager(mongoClient, databaseName, "cache-", mongoDbReentrantLock(), 60 * 60);
    }

    @Bean
    public MongoDbDeque<String> generateSentenceForWordsWithoutOneQueue() {
        var collection = mongoClient.getDatabase(databaseName).getCollection("queue-generateSentenceForWordsWithoutOne");
        return new MongoDbDeque<>(String.class, mongoClient, collection);
    }

}
