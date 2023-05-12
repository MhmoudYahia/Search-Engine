package com.goat.searchengine.api.model;

public class Query implements Comparable<Query>{
    private String query;
    private String url;
    private String title;
    private String description;
    private double score;

    public Query(String query,String url,String title,String description,double score)
    {
        this.query=query;
        this.description=description;
        this.url=url;
        this.title=title;
        this.score=score;
    }
    public String getQuery(){return query;}
    public String getDescription(){return description;}
    public String getTitle(){return title;}
    public String getUrl(){return url;}

    public double getScore(){return score;}

    @Override
    public int compareTo(Query q) {
        if(this.score - q.score > 0)
            return -1;
        else
            return 1;
    }
}
