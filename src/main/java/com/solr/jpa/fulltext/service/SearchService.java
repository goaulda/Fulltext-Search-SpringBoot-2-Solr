package com.solr.jpa.fulltext.service;

import com.solr.jpa.fulltext.model.Product;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.BooleanJunction;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.hibernate.search.query.facet.FacetingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchService implements ApplicationListener<ApplicationReadyEvent> {

    public static final String FACET_DESC = "description";
    public static final String FACET_PROD = "productName";

    private final EntityManager entityManager;


    @Autowired
    public SearchService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private void initData() throws InterruptedException {
        Search.getFullTextEntityManager(entityManager).createIndexer().startAndWait();
    }


    public SearchResponse search(SearchRequest request) {

        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Product.class).get();

        BooleanJunction bool = queryBuilder.bool().must(queryBuilder.all().createQuery());

        if (request.description != null) {
            Query categoryQuery = queryBuilder.keyword().onField(FACET_DESC).matching(request.description).createQuery();
            bool = bool.must(categoryQuery);
        }
        if (request.name != null) {
            Query dateQuery = queryBuilder.keyword().onField(FACET_PROD).matching(request.name).createQuery();
            bool = bool.must(dateQuery);
        }
        //if (request.keyword != null) {
          //  Query dateQuery = queryBuilder.keyword().onField(FIELD_TEXT).matching(request.keyword).createQuery();
           // bool = bool.must(dateQuery);
        //}

        FacetingRequest descFacetRequest = queryBuilder.facet().name(FACET_DESC).onField(FACET_DESC)
                .discrete().createFacetingRequest();

        FacetingRequest nameFacetRequest = queryBuilder.facet().name(FACET_PROD).onField(FACET_PROD)
                .discrete().createFacetingRequest();

        FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(bool.createQuery(), Product.class);
        fullTextQuery.getFacetManager().enableFaceting(descFacetRequest);
        fullTextQuery.getFacetManager().enableFaceting(nameFacetRequest);

        List<SearchFacet> descFacet = fullTextQuery.getFacetManager().getFacets(FACET_DESC)
                .stream().map(f -> new SearchFacet(f.getValue(), f.getCount())).collect(Collectors.toList());
        List<SearchFacet> nameFacet = fullTextQuery.getFacetManager().getFacets(FACET_PROD)
                .stream().map(f -> new SearchFacet(f.getValue(), f.getCount())).collect(Collectors.toList());

        return new SearchResponse(descFacet, nameFacet, fullTextQuery.getResultList());

    }


    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            initData();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
