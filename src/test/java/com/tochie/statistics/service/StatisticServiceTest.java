package com.tochie.statistics.service;

import com.tochie.statistics.dto.TransactionDTO;
import com.tochie.statistics.dto.TransactionResponse;
import com.tochie.statistics.model.Statistic;
import com.tochie.statistics.repository.StatisticRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.*;


class StatisticServiceTest {

    private StatisticRepository statisticRepository;
    private StatisticService statisticService;

    static final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    @BeforeEach
    void setUp() {

        statisticService = Mockito.spy(StatisticService.class);
        statisticRepository = Mockito.spy(StatisticRepository.class);

        Whitebox.setInternalState(statisticService, "statisticRepository", statisticRepository);

    }

    @Test
    void createStaticsInvalidDataTest()  {
        TransactionDTO transactionDTO = new TransactionDTO();

        HttpStatus statistic = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, statistic);

        transactionDTO.setAmount("12s");
        transactionDTO.setTimestamp("2021-09-16T12:03:25.312Z");
        HttpStatus statistic2 = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, statistic2);

    }

    @Test
    void createStaticsOldTransactionTest()  {


        TransactionDTO transactionDTO = new TransactionDTO();

        Instant instant = Instant.ofEpochMilli ( System.currentTimeMillis() );
        transactionDTO.setAmount("168.090");
        transactionDTO.setTimestamp(instant.toString());
        HttpStatus statistic3 = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, statistic3);





    }

    @Test
    void createStatisticSuccess()  {


        TransactionDTO transactionDTO = new TransactionDTO();

        Date date = new Date();
        transactionDTO.setAmount("168.090");
        transactionDTO.setTimestamp(sdf.format(date));

        HttpStatus statistic4 = statisticService.statistic(transactionDTO);
        Assertions.assertEquals(HttpStatus.CREATED, statistic4);

    }

    @Test()
    void getStatisticsFailed() {
        Mockito.doReturn(Collections.EMPTY_LIST).when(statisticRepository).findAll();
        TransactionResponse statistics1 = statisticService.getStatistics();

        assertEquals(0, statistics1.getCount());
        Assertions.assertNull(statistics1.getAvg());

    }

    @Test()
    void getStatisticsSuccess() {

        Mockito.doReturn(statisticCollection()).when(statisticRepository).findAll();
        TransactionResponse statistics = statisticService.getStatistics();

        Assertions.assertTrue(statistics.getCount() > 0);

        Assertions.assertNotNull(statistics.getAvg());
        Assertions.assertNotNull(statistics.getSum());
        Assertions.assertNotNull(statistics.getMax());
        Assertions.assertNotNull(statistics.getMin());

    }


    @Test
    void delete() {

        statisticService.delete();

        Mockito.verify(statisticRepository, Mockito.times(1)).delete();
    }



    static Collection<Statistic> statisticCollection(){
        Map<Integer, Statistic> statisticMap = new ConcurrentHashMap<>();

        Statistic statistic = new Statistic(1, BigDecimal.valueOf(123.43), Timestamp.valueOf(LocalDateTime.now()));
        statisticMap.put(statistic.getId(), statistic);

        Statistic statistic1 = new Statistic(2, BigDecimal.valueOf(23.43), Timestamp.valueOf(LocalDateTime.now()));
        statisticMap.put(statistic1.getId(), statistic1);

        Statistic statistic2 = new Statistic(3, BigDecimal.valueOf(53.43), Timestamp.valueOf(LocalDateTime.now()));
        statisticMap.put(statistic2.getId(), statistic2);

        return statisticMap.values();
    }


}