package com.goat.searchengine.api.controller;

import com.goat.searchengine.api.document.WordDocument;
import com.goat.searchengine.api.model.Query;
import com.goat.searchengine.api.repository.Repository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.Math;
import java.util.*;


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

@GetMapping("/api")
public List<Query> search(String text)
{
    //get the total document count from the database
    LinkedHashMap meta = (LinkedHashMap) repository.getDocMetaData();
    int doc_count = (int)meta.get("Doc_Cnt") ;

    //split the search text into words
    String words[] = text.split(" ");
    HashMap<String, Pair<DBObject,Double>> map = new HashMap<>();
    List<Query> results = new ArrayList<>();

    //start ranking the pages
    for(String word:words) {
        WordDocument item = repository.findByWord(word);
        List<DBObject> arr = item.getPagesContainingThisWord();

        //calculate idf for the word
        double doc_freq = item.getTotal_Appearance_in_All_Pages();
        double idf = Math.log(doc_count / doc_freq);

        for (DBObject obj : arr) {
            String url = (String) obj.get("Page_URL");
            double tf = (double) obj.get("Normalized_TF");
            double tf_idf = tf * idf;
            double score = map.containsKey(url) ? map.get(url).getSecond() : 0;
            Pair<DBObject, Double> pair = Pair.of(obj, score + tf_idf);
            map.put(url, pair);
        }
    }
    for(Map.Entry<String,Pair<DBObject,Double>> entry : map.entrySet())
    {
        System.out.println(entry);
        String desc = (String)entry.getValue().getFirst().get("Page_Description");
        String title = (String)entry.getValue().getFirst().get("Page_Title");
        String url = entry.getKey();
        double score = entry.getValue().getSecond();
        Query q = new Query("",url,title,desc,score);
        results.add(q);
    }
    Collections.sort(results);
    return results;
}
}
