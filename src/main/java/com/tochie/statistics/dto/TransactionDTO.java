package com.tochie.statistics.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionDTO {


    @JsonProperty(required = true)
    private String amount;

    @JsonProperty(required = true)
    @JsonFormat(pattern = "YYYY-MM-DDThh:mm:ss.sssZ")
    private String timestamp;
}
