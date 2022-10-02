package com.foilen.studies.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class SpeakTextCacheRepositoryImpl implements SpeakTextCacheRepository {

    @Autowired
    private MongoDatabaseFactory dbFactory;
    @Autowired
    private MongoConverter converter;
    private GridFsTemplate gridFsTemplate;

    @PostConstruct
    public void init() {
        // TODO speakTextCache - Needs precreation with indexes?
        gridFsTemplate = new GridFsTemplate(dbFactory, converter, "speakTextCache");
    }

}
