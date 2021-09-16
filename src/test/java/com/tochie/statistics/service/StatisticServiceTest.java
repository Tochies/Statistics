package com.tochie.statistics.service;

import com.tochie.statistics.dto.TransactionDTO;
import com.tochie.statistics.dto.TransactionResponse;
import com.tochie.statistics.repository.StatisticRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class StatisticServiceTest {

    @Autowired
    StatisticRepository statisticRepository;

    @Autowired
    StatisticService statisticService;

    static final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @BeforeEach
    void setUp() {

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount("12.338");
        transactionDTO.setTimestamp(sdf.format(new Date()));
        statisticService.statistic(transactionDTO);

        TransactionDTO transactionDTO1 = new TransactionDTO();
        transactionDTO1.setAmount("480.0");
        transactionDTO1.setTimestamp(sdf.format(new Date()));
        statisticService.statistic(transactionDTO1);

        TransactionDTO transactionDTO2 = new TransactionDTO();
        transactionDTO2.setAmount("180.00");
        transactionDTO2.setTimestamp(sdf.format(new Date()));
        statisticService.statistic(transactionDTO2);

    }

    @Test
    void statistic()  {
        TransactionDTO transactionDTO = new TransactionDTO();

        HttpStatus statistic = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, statistic);

        transactionDTO.setAmount("12s");
        transactionDTO.setTimestamp("2021-09-16T12:03:25.312Z");
        HttpStatus statistic2 = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, statistic2);

        Instant instant = Instant.ofEpochMilli ( System.currentTimeMillis() );
        transactionDTO.setAmount("168.090");
        transactionDTO.setTimestamp(instant.toString());
        HttpStatus statistic3 = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, statistic3);


        Date date = new Date();
        transactionDTO.setAmount("168.090");
        transactionDTO.setTimestamp(sdf.format(date));

        HttpStatus statistic4 = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.ACCEPTED, statistic4);

    }

    @Test()
    void getStatistics() {
        TransactionResponse statistics = statisticService.getStatistics();
        System.out.println(statistics);

    }

    @Test
    void delete() {
        statisticService.delete();
    }


}