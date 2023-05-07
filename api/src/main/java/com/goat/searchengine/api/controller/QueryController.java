package com.goat.searchengine.api.controller;

import com.goat.searchengine.api.model.Query;
import com.goat.searchengine.service.QueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueryController {
    private QueryService queryService;

    @Autowired
    public QueryController(QueryService queryService){
        this.queryService=queryService;
    }
    @GetMapping("/")
    public List<Query> getResult(@RequestParam String q){

        return queryService.getQueryResults(q);
    }
}
