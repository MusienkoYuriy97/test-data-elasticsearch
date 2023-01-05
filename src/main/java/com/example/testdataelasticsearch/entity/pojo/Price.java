package com.example.testdataelasticsearch.entity.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class Price {
    private String id;
    private String currency;
    private BigDecimal amount;
}
