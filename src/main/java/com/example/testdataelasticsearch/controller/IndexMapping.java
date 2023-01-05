package com.example.testdataelasticsearch.controller;

import com.example.testdataelasticsearch.service.IndexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/index")
@RequiredArgsConstructor
public class IndexMapping {
    private final IndexService indexService;

    @PostMapping("recreate")
    public void recreateAllIndices() {
        indexService.recreateIndices(Boolean.TRUE);
    }
}