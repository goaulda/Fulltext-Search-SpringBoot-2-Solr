package com.solr.jpa.fulltext.controller;

import com.solr.jpa.fulltext.service.SearchRequest;
import com.solr.jpa.fulltext.service.SearchResponse;
import com.solr.jpa.fulltext.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ProductController {

    @Autowired
    private SearchService searchService;

    @RequestMapping(value = "/search", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ResponseBody
    public SearchResponse search(@RequestParam(value = "name", required = false) String name,
                                 @RequestParam(value = "desc", required = false) String desc
                                 ){
        return searchService.search(new SearchRequest(name, desc));
    }
}
