package com.example.testdataelasticsearch.controller;

import com.example.testdataelasticsearch.entity.pojo.Course;
import com.example.testdataelasticsearch.entity.search.SearchRequestDTO;
import com.example.testdataelasticsearch.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexMapping {
    private final IndexService indexService;

    @PostMapping("{indexName}/search")
    public List<Course> search(@PathVariable final String indexName, @RequestBody final SearchRequestDTO dto) {
        return indexService.search(indexName, dto);
    }

    @PostMapping("{indexName}/search/{date}")
    public List<Course> searchRange(@PathVariable final String indexName,
                                    @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") final Date date) {
        return indexService.searchRange(indexName, date);
    }

    @PostMapping("/recreate")
    public void recreateAllIndices() {
        indexService.recreateIndices(Boolean.TRUE);
    }
}