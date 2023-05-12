package com.goat.searchengine.api.repository;

import com.goat.searchengine.api.document.WordDocument;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.LinkedHashMap;

public interface Repository extends MongoRepository<WordDocument, String> {
    @Query("{'Word' : ?0}")
    WordDocument findByWord(String word);

    @Query("{'info':'Doc_meta_data'}")
    Object getDocMetaData();
}
