package com.solr.jpa.fulltext.repo;

import com.solr.jpa.fulltext.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, String> {

}
