package com.example.testdataelasticsearch.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElasticsearchIndices {
    public static final String USER_TEST = "user-test";

    public static final String COURSE = "course";

    public static final List<String> INDICES_TO_CREATE = List.of(COURSE);
}
