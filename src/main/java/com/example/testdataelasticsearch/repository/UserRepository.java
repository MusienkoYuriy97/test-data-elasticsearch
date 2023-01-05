package com.example.testdataelasticsearch.repository;

import com.example.testdataelasticsearch.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserRepository extends ElasticsearchRepository<User, String> {
}
