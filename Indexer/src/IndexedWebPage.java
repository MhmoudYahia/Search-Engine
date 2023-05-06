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
public class IndexedWebPage {
    String url;

    public int score;
    int TF;
    double normalized_TF;
    double pageRank;   //later for ranker
    String title;
    String disc;

    // positions of this word in this page   (word => pages)
    ArrayList<Integer> WordPositions;


    public IndexedWebPage() {

    }

    public IndexedWebPage(String url, int first_pos) {
        this.TF = 1;
        this.url = url;
        this.WordPositions = new ArrayList<>();
        this.WordPositions.add(first_pos);
    }

    public void setScore(int s ){
        score = s;
    }
    public int getScore(){
       return score;
    }

    public void incrementTF() {
        this.TF++;
    }

    public void addPosition(int new_pos) {
        this.WordPositions.add(new_pos);
        this.incrementTF();
    }

    public void normalize(long totalCnt) {
        this.normalized_TF = (double) this.TF / totalCnt;
    }

    public String getUrl() {
        return url;
    }

    public int getTF() {
        return TF;
    }

    public ArrayList<Integer> getWordPositions() {
        return WordPositions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public void setDesc(String s) {
        this.disc = s;
    }
    public double get_normalizedTf (){
            return normalized_TF;
    }
    public String getDisc() {
        return disc;
    }

    public static DBObject toDocument(IndexedWebPage iWP) {
        return new BasicDBObject("Page_URL", iWP.getUrl())
                .append("TF", iWP.getTF())
                .append("Score", iWP.getScore())
                .append("Normalized_TF", iWP.get_normalizedTf())
                .append("Word_Positions_In_this_Page", iWP.getWordPositions())
                .append("Page_Title", iWP.getTitle())
                .append("Page_Description", iWP.getDisc());
    }
}
