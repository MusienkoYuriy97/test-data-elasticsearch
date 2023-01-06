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
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Optional;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.CrossFields;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SearchUtil {
    private final static Logger log = LoggerFactory.getLogger(SearchUtil.class.getSimpleName());

    public static org.springframework.data.elasticsearch.core.query.Query buildQuery(final SearchRequestDTO dto) {
        try {
            final var nativeQueryBuilder = NativeQuery.builder()
                    .withQuery(Optional
                            .ofNullable(getQuery(dto))
                            .orElseThrow(() -> new RuntimeException("Query cannot be null")));
            applySort(dto, nativeQueryBuilder);
            return nativeQueryBuilder.build();
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }

    public static org.springframework.data.elasticsearch.core.query.Query buildQueryRange(final String field, final Date date) {
        try {
            return NativeQuery.builder()
                    .withQuery(q -> q.range(r -> r.field(field).gte(JsonData.of(date)))).build();
        } catch (Exception ex) {
            log.error("Cannot build a query", ex);
            return null;
        }
    }

    private static void applySort(SearchRequestDTO dto, NativeQueryBuilder nativeQueryBuilder) {
        if (isNull(dto.getSortBy())) {
            return;
        }
        final var builder = new SortOptions.Builder();
        builder.field((f) -> f.field(dto.getSortBy())
                .order(nonNull(dto.getSortOrder()) ? dto.getSortOrder() : SortOrder.Asc));
        nativeQueryBuilder.withSort(builder.build());
    }

    private static Query getQuery(final SearchRequestDTO dto) {
        if (isNull(dto)) {
            return null;
        }
        if (CollectionUtils.isEmpty(dto.getFields())) {
            return null;
        }
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
