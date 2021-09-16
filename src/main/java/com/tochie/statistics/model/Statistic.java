package com.tochie.statistics.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;

@ToString
@Getter
@Setter
public class Statistic {

    private Integer id;

    private BigDecimal amount;

    private Timestamp timestamp;

    public Statistic() {
    }

    public Statistic(Integer id, BigDecimal amount, Timestamp timestamp) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
    }


}
