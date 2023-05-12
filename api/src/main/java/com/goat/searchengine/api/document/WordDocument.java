package com.goat.searchengine.api.document;

import com.mongodb.DBObject;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "web_pages")
public class WordDocument {
    @Id
    private String id;
    private String Wordsss;
    @Field("Total_Apperance_in_All_Pages")
    private int Total_Appearance_in_All_Pages;
    @Field("Pages_Containing_This_Word")
    private List<DBObject> Pages_Containing_This_Word;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return Wordsss;
    }

    public void setWord(String word) {
        this.Wordsss = word;
    }


    public int getTotal_Appearance_in_All_Pages() {
        return Total_Appearance_in_All_Pages;
    }

    public void setTotal_Appearance_in_All_Pages(int total_Appearance_in_All_Pages) {
        Total_Appearance_in_All_Pages = total_Appearance_in_All_Pages;
    }

    public List<DBObject> getPagesContainingThisWord() {
        return Pages_Containing_This_Word;
    }

    public void setPagesContainingThisWord(List<DBObject> pagesContainingThisWord) {
        this.Pages_Containing_This_Word = pagesContainingThisWord;
    }

}
