package com.example.testdataelasticsearch.service;

import com.example.testdataelasticsearch.entity.pojo.Course;
import com.example.testdataelasticsearch.entity.search.SearchRequestDTO;
import com.example.testdataelasticsearch.mapper.SearchHintsMapper;
import com.example.testdataelasticsearch.util.LoadFileUtil;
import com.example.testdataelasticsearch.util.SearchUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.testdataelasticsearch.util.ElasticsearchIndices.INDICES_TO_CREATE;

@Service
@RequiredArgsConstructor
public class IndexService {
    private static final Logger log = LoggerFactory.getLogger(IndexService.class.getSimpleName());
    private final ElasticsearchOperations elasticsearchOperations;
    @Value("${elasticsearch.deleteExistingIndex}")
    public boolean deleteExisting;

    @PostConstruct
    public void createIndices() {
        recreateIndices(deleteExisting);
    }

    public List<Course> search(final String indexName, final SearchRequestDTO dto) {
        final var query = Optional.ofNullable(SearchUtil.buildQuery(dto))
                .orElseThrow(() -> new RuntimeException("Query is empty"));
        return executeQuery(indexName, query);
    }

    public List<Course> searchRange(final String indexName, final Date date) {
        final var query = Optional.ofNullable(SearchUtil.buildQueryRange("createdAt", date))
                .orElseThrow(() -> new RuntimeException("Query is empty"));
        return executeQuery(indexName, query);
    }

    public void recreateIndices(final boolean deleteExisting) {
        final var settings = LoadFileUtil.loadAsString("static/es-settings.json");
        INDICES_TO_CREATE.forEach(indexName -> {
            try {
                final var index = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));
                // Delete existing index if needed
                if (index.exists()) {
                    if (!deleteExisting) {
                        return;
                    }
                    index.delete();
                }
                createIndex(index, indexName, settings);
            } catch (final Exception e) {
                log.error("Cannot create an index", e);
            }
        });
    }

    private List<Course> executeQuery(String indexName, Query query) {
        final var searchHits = elasticsearchOperations
                .search(query, Course.class, IndexCoordinates.of(indexName));
        return Optional.ofNullable(SearchHintsMapper.toList(searchHits))
                .orElseThrow(() -> new RuntimeException("There is no results according your query"));
    }

    private void createIndex(final IndexOperations index, final String indexName, final String settings) {
        // Get mapping schema from .json
        final var mappings = LoadFileUtil.loadAsString("static/mappings/" + indexName + ".json");

        if (Objects.nonNull(mappings)) {
            // Create new one index in Elasticsearch
            index.create(Document.parse(settings), Document.parse(mappings));
            log.info("Index with name: " + indexName + " war created");
        }
    }

}
