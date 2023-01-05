package com.example.testdataelasticsearch.service;

import com.example.testdataelasticsearch.util.LoadFileUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

    public void recreateIndices(final boolean deleteExisting) {
        final String settings = LoadFileUtil.loadAsString("static/es-settings.json");
        for (final String indexName : INDICES_TO_CREATE) {
            try {
                final IndexOperations index = elasticsearchOperations.indexOps(IndexCoordinates.of(indexName));
                // Delete existing index if needed
                if (index.exists()) {
                    if (!deleteExisting) {
                        continue;
                    }
                    index.delete();
                }
                createIndex(index, indexName, settings);
            } catch (final Exception e) {
                log.error("Cannot create an index", e);
            }
        }
    }

    private void createIndex(final IndexOperations index, final String indexName, final String settings) {
        // Get mapping schema from .json
        final String mappings = LoadFileUtil.loadAsString("static/mappings/" + indexName + ".json");

        if (Objects.nonNull(mappings)) {
            // Create new one index in Elasticsearch
            index.create(Document.parse(settings), Document.parse(mappings));
            log.info("Index with name: " + indexName + " war created");
        }
    }

}
