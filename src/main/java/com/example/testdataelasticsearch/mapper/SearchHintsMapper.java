package com.example.testdataelasticsearch.mapper;


import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchHintsMapper {
    public static <T> List<T> toList(final SearchHits<T> searchHits) {
        final List<T> list = new ArrayList<>();
        for (final SearchHit<T> searchHit : searchHits.getSearchHits()) {
            final T content = searchHit.getContent();
            if (nonNull(content)) {
                list.add(content);
            }
        }
        return list;
    }
}
