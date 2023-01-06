package com.example.testdataelasticsearch.entity.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Course {
    private String id;
    private String name;
    private Price price;
    private Author author;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
}
