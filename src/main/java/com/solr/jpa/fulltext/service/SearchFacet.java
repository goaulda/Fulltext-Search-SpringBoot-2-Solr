package com.solr.jpa.fulltext.service;

public class SearchFacet {

    public final String value;
    public final Integer count;


    public SearchFacet(String value, Integer count) {
        this.value = value;
        this.count = count;
    }
}
