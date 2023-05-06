/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import java.util.ArrayList;

/**
 *
 * @author mahmoud
 */
public final class DataBaseObject {

    String word;
    double idf;
    int cnt = 0;
    ArrayList<DBObject> pageData = new ArrayList<>();

    public DataBaseObject(String w, IndexedWebPage wp) {
        this.setWord(w);
        this.addPage(wp);
    }

    public DataBaseObject(String w) {
        this.setWord(w);
    }

    public void setWord(String w) {
        this.word = w;
    }

    public void addPage(IndexedWebPage indexerdWP) {
        this.pageData.add(IndexedWebPage.toDocument(indexerdWP));
        cnt++;
    }

    /*
     * The IDF of a term is calculated as the logarithm of the total number of documents
     * in the collection divided by the number of documents that contain the term. The formula for IDF is
     * IDF(term) = log(N / df(term))
     */
    public void CalculateIDF(int tot) {
        this.idf = (double) Math.log(tot / this.cnt);
    }

    public DBObject convertToDocument() {

        DBObject doc = new BasicDBObject("Word", this.word)
                .append("Total_Apperance_in_All_Pages", this.cnt)
                .append("IDF", this.idf)
                .append("Pages_Containing_This_Word", this.pageData);
        return doc;
    }
}
