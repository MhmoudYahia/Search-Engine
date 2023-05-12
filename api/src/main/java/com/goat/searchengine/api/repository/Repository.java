package com.goat.searchengine.api.repository;

import com.goat.searchengine.api.document.WordDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Repository extends MongoRepository<WordDocument, String> {
}
