package com.goat.searchengine.api.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "web_pages")
public class Word {
    @Id
    private String id;
    private String Word;
    private int Total_Appearance_in_All_Pages;
    private List<Page> Pages_Containing_This_Word;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return Word;
    }

    public void setWord(String word) {
        this.Word = word;
    }



    public void setTotalAppearanceInAllPages(int Total_Apperance_in_All_Pages) {
        this.Total_Appearance_in_All_Pages = Total_Apperance_in_All_Pages;
    }
    public int getTotalAppearanceInAllPages() {
        return Total_Appearance_in_All_Pages;
    }

    public List<Page> getPagesContainingThisWord() {
        return Pages_Containing_This_Word;
    }

    public void setPagesContainingThisWord(List<Page> pagesContainingThisWord) {
        this.Pages_Containing_This_Word = pagesContainingThisWord;
    }

    public static class Page {
        private String pageURL;
        private int score;
        private List<Integer> wordPositionsInThisPage;
        private String pageTitle;
        private String pageDescription;

        public String getPageURL() {
            return pageURL;
        }

        public void setPageURL(String pageURL) {
            this.pageURL = pageURL;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public List<Integer> getWordPositionsInThisPage() {
            return wordPositionsInThisPage;
        }

        public void setWordPositionsInThisPage(List<Integer> wordPositionsInThisPage) {
            this.wordPositionsInThisPage = wordPositionsInThisPage;
        }

        public String getPageTitle() {
            return pageTitle;
        }

        public void setPageTitle(String pageTitle) {
            this.pageTitle = pageTitle;
        }

        public String getPageDescription() {
            return pageDescription;
        }

        public void setPageDescription(String pageDescription) {
            this.pageDescription = pageDescription;
        }
    }
}
