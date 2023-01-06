package com.example.testdataelasticsearch.util;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import com.example.testdataelasticsearch.entity.search.SearchRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;

import java.util.Date;
import java.util.Optional;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.CrossFields;
import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchUtil {
    private final static Logger log = LoggerFactory.getLogger(SearchUtil.class.getSimpleName());

    public static org.springframework.data.elasticsearch.core.query.Query sortedQuery(final SearchRequestDTO dto) {
        try {
            return buildSortedNativeQuery(dto);
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }

    public static org.springframework.data.elasticsearch.core.query.Query rangedQuery(final String field,
                                                                                      final Date date) {
        try {
            return NativeQuery.builder()
                    .withQuery(buildDateQuery(field, date))
                    .build();
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }

    public static org.springframework.data.elasticsearch.core.query.Query createdSinceQuery(final String field,
                                                                                            final SearchRequestDTO dto,
                                                                                            final Date date) {
        try {
            final var searchQuery = buildSortedNativeQuery(dto);
            final var dateQuery = buildDateQuery(field, date);
            return NativeQuery.builder()
                    .withQuery(q -> q.bool(bq ->bq.must(searchQuery.getQuery()).must(dateQuery)))
                    .withSort(searchQuery.getSortOptions())
                    .build();
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }

    private static NativeQuery buildSortedNativeQuery(SearchRequestDTO dto) {
        final var nativeQueryBuilder = NativeQuery.builder()
                .withQuery(Optional
                        .ofNullable(generateQuery(dto))
                        .orElseThrow(() -> new RuntimeException("Query cannot be null")));
        applySort(dto, nativeQueryBuilder);
        return nativeQueryBuilder.build();
    }

    private static Query buildDateQuery(final String field, final Date date) {
        return new Query.Builder()
                .range(r -> r.field(field).gte(JsonData.of(date)))
                .build();
    }

    private static void applySort(final SearchRequestDTO dto, final NativeQueryBuilder nativeQueryBuilder) {
        final var builder = new SortOptions.Builder();
        builder.field((f) -> f.field(dto.getSortBy())
                .order(nonNull(dto.getSortOrder()) ? dto.getSortOrder() : SortOrder.Asc));
        nativeQueryBuilder.withSort(builder.build());
    }

    private static Query generateQuery(final SearchRequestDTO dto) {
        if (dto.getFields().size() > 1) {
            return multiMatchQuery(dto);
        }
        return matchQuery(dto);
    }

    private static Query matchQuery(final SearchRequestDTO dto) {
        return dto.getFields().stream().findFirst()
                .map(field ->
                        Query.of(query -> query.match(
                                MatchQuery.of(matchQuery -> matchQuery
                                        .field(field)
                                        .query(dto.getTerm())
                                        .operator(And)))))
                .orElse(null);
    }

    private static Query multiMatchQuery(final SearchRequestDTO dto) {
        final var operator = new MultiMatchQuery.Builder()
                .query(dto.getTerm())
                .type(CrossFields)
                .operator(And);
        dto.getFields().forEach(operator::fields);
        return Query.of(query -> query.multiMatch(multiMatch -> operator));
    }
}
