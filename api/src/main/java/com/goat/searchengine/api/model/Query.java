package com.goat.searchengine.api.model;

public class Query {
    private String query;
    private String url;
    private String title;
    private String description;

    public Query(String query,String url,String title,String description)
    {
        this.query=query;
        this.description=description;
        this.url=url;
        this.title=title;
    }
    public String getQuery(){return query;}
    public String getDescription(){return description;}
    public String getTitle(){return title;}
    public String getUrl(){return url;}
}
