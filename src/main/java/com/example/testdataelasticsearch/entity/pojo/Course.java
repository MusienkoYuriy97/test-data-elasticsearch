package com.example.testdataelasticsearch.entity.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Course {
    private String id;
    private String name;
    private Price price;
    private Author author;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
}
