package com.example.testdataelasticsearch.entity.search;

import co.elastic.clients.elasticsearch._types.SortOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SearchRequestDTO {
    private Set<String> fields;
    private String term;
    private String sortBy;
    private SortOrder sortOrder;
}
