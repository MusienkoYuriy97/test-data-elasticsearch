package com.example.testdataelasticsearch.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.json.JsonData;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RangeQueryHelper {
    static Query buildRangedQuery(final String field, final Date date) {
        return new Query.Builder()
                .range(r -> r.field(field).gte(JsonData.of(date)))
                .build();
    }
}
