package com.goat.searchengine.api.model;

public class Query implements Comparable<Query>{
    private String url;
    private String title;
    private String description;
    private String paragraph;
    private double score;

    public Query(String url,String title,String description,String paragraph,double score)
    {
        this.description=description;
        this.url=url;
        this.title=title;
        this.paragraph = paragraph;
        this.score=score;
    }
    public String getDescription(){return description;}
    public String getTitle(){return title;}
    public String getUrl(){return url;}
    public String getParagraph() {
        return paragraph;
    }
    public double getScore(){return score;}

    @Override
    public int compareTo(Query q) {
        if(this.score - q.score > 0)
            return -1;
        else
            return 1;
    }
}
