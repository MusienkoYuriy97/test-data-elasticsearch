package com.example.testdataelasticsearch.util;

import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.example.testdataelasticsearch.entity.search.SearchRequestDTO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static com.example.testdataelasticsearch.util.MatchQueryHelper.generateQuery;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NativeQueryHelper {
    static NativeQuery buildSortedNativeQuery(final SearchRequestDTO dto) {
        final var nativeQueryBuilder = NativeQuery.builder()
                .withQuery(Optional
                        .ofNullable(generateQuery(dto))
                        .orElseThrow(() -> new RuntimeException("Query cannot be null")));
        applySort(dto, nativeQueryBuilder);
        return nativeQueryBuilder.build();
    }

    static NativeQuery buildBoolNativeQuery(List<SortOptions> sortOptions, final Query query1, final Query... query2) {
        final var nativeQueryBuilder = NativeQuery.builder()
                .withQuery(q -> q.bool(bq -> bq.must(query1, query2)));
        if (!CollectionUtils.isEmpty(sortOptions)) {
            nativeQueryBuilder.withSort(sortOptions);
        }
        return nativeQueryBuilder.build();
    }

    private static void applySort(final SearchRequestDTO dto, final NativeQueryBuilder nativeQueryBuilder) {
        if (isNull(dto.getSortBy()) || StringUtils.isBlank(dto.getSortBy())) {
            return;
        }
        final var builder = new SortOptions.Builder();
        builder.field((f) -> f.field(dto.getSortBy())
                .order(nonNull(dto.getSortOrder()) ? dto.getSortOrder() : SortOrder.Asc));
        nativeQueryBuilder.withSort(builder.build());
    }
}
