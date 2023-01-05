package com.example.testdataelasticsearch.configuration.elasticsearch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@ComponentScan(basePackages = "com.example.testdataelasticsearch")
@EnableElasticsearchRepositories(
        basePackages = "com.example.testdataelasticsearch.repository"
)
public class ElasticsearchConfig extends ElasticsearchConfiguration {
    @Value("${elasticsearch.url}")
    public String elasticsearchUrl;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchUrl)
                .build();
    }
}
