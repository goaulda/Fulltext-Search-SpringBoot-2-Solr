package com.solr.jpa.fulltext.service;

public class SearchRequest {
    public final String name;

    public final String description;


    public SearchRequest(String name, String description) {

        this.name = name;
        this.description = description;
    }

    public static SearchRequest all() {
        return new SearchRequest(null, null);
    }

    public SearchRequest searchDesc(String description) {
        return new SearchRequest(this.name, description);
    }

    public SearchRequest searchName(String name) {
        return new SearchRequest(name, this.description);
    }

}
