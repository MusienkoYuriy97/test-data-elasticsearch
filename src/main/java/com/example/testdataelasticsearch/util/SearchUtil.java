package com.example.testdataelasticsearch.util;

import com.example.testdataelasticsearch.entity.search.SearchRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.query.Query;

import java.util.Date;

import static com.example.testdataelasticsearch.util.NativeQueryHelper.buildBoolNativeQuery;
import static com.example.testdataelasticsearch.util.NativeQueryHelper.buildSortedNativeQuery;
import static com.example.testdataelasticsearch.util.RangeQueryHelper.buildRangedQuery;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchUtil {
    private final static Logger log = LoggerFactory.getLogger(SearchUtil.class.getSimpleName());

    public static Query sortedQuery(final SearchRequestDTO dto) {
        try {
            return buildSortedNativeQuery(dto);
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }

    public static Query rangedQuery(final String field, final Date date) {
        try {
            return NativeQuery.builder()
                    .withQuery(buildRangedQuery(field, date))
                    .build();
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }

    public static Query createdSinceQuery(final String field, final SearchRequestDTO dto, final Date date) {
        try {
            final var searchQuery = buildSortedNativeQuery(dto);
            final var dateQuery = buildRangedQuery(field, date);
            return buildBoolNativeQuery(searchQuery.getSortOptions(),
                    searchQuery.getQuery(), dateQuery).setPageable(PageRequest.of(dto.getPage(), dto.getSize()));
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }
}
