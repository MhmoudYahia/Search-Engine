package com.goat.searchengine.api.service;

import com.goat.searchengine.api.model.Query;
import org.springframework.stereotype.Service;

//import com.mongodb.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QueryService {
    private List<Query> queryList;

//    public static DB database;
//    public static DBCollection webpagesCollection;
//    public static MongoClient mongoClient;
    public QueryService(){
        //setDB();
        System.out.println("ranker connecting data base.");

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


    /*
     * The IDF of a term is calculated as the logarithm of the total number of documents
     * in the collection divided by the number of documents that contain the term. The formula for IDF is
     * IDF(term) = log(N / df(term))
     */
//    public double CalculateIDF(int tot, int count) {
//        double idf = Math.log((double)tot / count);
//        return idf;
//    }
//
//    public static void setDB() {
//        System.out.println("ranker connecting data base.");
//        mongoClient = new MongoClient(Constants.DATABASE_HOST_ADDRESS, Constants.DATABASE_PORT_NUMBER);
//        database = mongoClient.getDB(Constants.DATABASE_NAME);
//
//        //IndexOptions indexOPtions =new IndexOptions();
//        webpagesCollection = database.getCollection(Constants.WEB_PAGES_COLLECTION);
//        //collection.createIndex(Indexes.ascending(Constants.F_URL),indexOPtions);
//        System.out.println("ranker connected to DB successfully.");
//
//        // create a word index to make the search in O(1)
//        DBObject index = new BasicDBObject("Word", 1);
//        DBObject options = new BasicDBObject("unique", false).append("name", "word_index");
//        webpagesCollection.createIndex(index, options);
//    }

    public List<Query> getQueryResults(String q) {
//        String[] search_words = q.split(" ");
//        HashMap<Object,Integer> links = new HashMap<>();
//
//        for(String search_text : search_words) {
//            BasicDBObject searchQuery = new BasicDBObject("Word", search_text);
//            DBCursor cursor = webpagesCollection.find(searchQuery);
//            while (cursor.hasNext()) {
//                DBObject value = cursor.next();
//                BasicDBList arr = (BasicDBList) value.get("Pages_Containing_This_Word");
//                for (Object obj : arr) {
//                    //String url = ((DBObject) obj).get("Page_URL").toString();
//                    int count = links.containsKey(obj) ? links.get(obj) : 0;
//                    links.put(obj, count + 1);
//                }
//            }
//        }
//        for(Map.Entry<Object,Integer> entry : links.entrySet())
//        {
//            System.out.println(entry.getKey());
//            System.out.println(entry.getValue());
//        }


        return queryList;
    }
}
