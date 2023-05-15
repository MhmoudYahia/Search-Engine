/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 *
 * @author mahmoud
 */
public final class DataBaseObject {

    String word;

    String stemmedWord;
    int cnt = 0;
    //ArrayList<DBObject> pageData = new ArrayList<>();
    LinkedHashSet<DBObject> pageData = new LinkedHashSet<>();

    public DataBaseObject(String w, IndexedWebPage wp) {
        this.setWord(w);
        this.setStemmedWord(wp.getStemmedWord());
        this.addPage(wp);
    }

    public DataBaseObject(String w) {
        this.setWord(w);
    }

    public void setWord(String w) {
        this.word = w;
    }

    public void setStemmedWord(String stemmedWord) {
        this.stemmedWord = stemmedWord;
    }

    public void addPage(IndexedWebPage indexerdWP) {
        this.pageData.add(IndexedWebPage.toDocument(indexerdWP));
        cnt = this.pageData.size();
    }

    public DBObject convertToDocument() {

        DBObject doc = new BasicDBObject("Word", this.word)
                .append("stemmedWord", this.stemmedWord)
                .append("Total_Apperance_in_All_Pages", this.cnt)
                .append("Pages_Containing_This_Word", this.pageData);
        return doc;
    }
}
