package com.example.testdataelasticsearch.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchHintsMapper {
    public static <T> List<T> toList(final SearchHits<T> searchHits) {
        if (isNull(searchHits)) {
            return null;
        }
        return searchHits
                .getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}
