package com.example.testdataelasticsearch.entity.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Course {
    private String id;
    private String name;
    private Price price;
    private Author author;
}
