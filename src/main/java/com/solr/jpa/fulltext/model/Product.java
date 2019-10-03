package com.solr.jpa.fulltext.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Facet;
import org.hibernate.search.annotations.Field;
import org.springframework.stereotype.Indexed;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "Products")

@Indexed
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    String uuid;

    @Field(analyze = Analyze.NO)
    @Facet(name = "productName")
    String productName;

    Double price;

    Long quantity;

    @Field(analyze = Analyze.NO)
    @Facet(name = "description")
    String description;

    String manufacturer;

    LocalDate lastSale;

}
