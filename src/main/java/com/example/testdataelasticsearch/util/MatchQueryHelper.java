package com.example.testdataelasticsearch.util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.testdataelasticsearch.entity.search.SearchRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static co.elastic.clients.elasticsearch._types.query_dsl.Operator.And;
import static co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType.CrossFields;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MatchQueryHelper {
    static Query generateQuery(final SearchRequestDTO dto) {
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
