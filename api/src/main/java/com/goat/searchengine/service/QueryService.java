package com.goat.searchengine.service;

import com.goat.searchengine.api.model.Query;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QueryService {
    private List<Query> queryList;
    public QueryService(){
        queryList=new ArrayList<>();
        Query query1=new Query("Yousef Rabia","https://github.com/Yousef-Rabia","Yousef Rabia Github","I am currently a junior computer engineering student at Cairo university. I am looking forward to\n" +
                "getting a good opportunity to increase my skills and experience and develop my passion for\n" +
                "programming.");
        Query query2=new Query("Yousef Rabia","https://www.linkedin.com/in/yousef-rabia-b260a9201/","Yousef Rabia LinkedIn","This application has no explicit mapping for /error, so you are seeing this as a fallback.");
        Query query3=new Query("Yousef Rabia","https://www.linkedin.com/in/yousef-rabia-b260a9201/","Query Processor","This module receives search queries, performs necessary preprocessing and searches the index for relevant\n" +
                "documents. Retrieve documents containing words that share the same stem with those in the search query. For\n" +
                "example, the search query “travel” should match (with lower degree) the words “traveler”, “traveling” … etc.");
        queryList.addAll(Arrays.asList(query1,query2,query3,query3,query3,query3,query3,query3,query3,query3,query3,query3,query3,
                query3,query3,query3));
    }
    public List<Query> getQueryResults(String q) {
        return queryList;
    }
}
