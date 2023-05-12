package com.goat.searchengine.api.controller;

import com.goat.searchengine.api.document.WordDocument;
import com.goat.searchengine.api.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {
    @Autowired
    private Repository repository;
//    private QueryService queryService;
//
//    @Autowired
//    public QueryController(QueryService queryService){
//        this.queryService=queryService;
//    }
//    @GetMapping("/")
//    public List<Query> getResult(@RequestParam String q){
//
//        return queryService.getQueryResults(q);
//    }
    @GetMapping("/lol")
    public List<WordDocument> test(){
        return repository.findAll();
    }
}
