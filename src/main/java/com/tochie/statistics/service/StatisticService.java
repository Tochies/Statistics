package com.tochie.statistics.service;

import com.tochie.statistics.dto.TransactionDTO;
import com.tochie.statistics.dto.TransactionResponse;
import com.tochie.statistics.model.Statistic;
import com.tochie.statistics.repository.StatisticRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Statistic service handles all logic related activities for transactions
 *
 * @author Tochie
 *
 */


@Slf4j
@Service
public class StatisticService {

    @Autowired
    private StatisticRepository statisticRepository;


    /**
     * HttpStatus statistic(TransactionDTO transactionDTO)
     *
     *
     * here:
     * amount – transaction amount; a string of arbitrary length that is parsable as a BigDecimal
     * timestamp – transaction time in the ISO 8601 format YYYY-MM-DDThh:mm:ss.sssZ in the  UTC timezone (this is not the current timestamp)
     *
     *
     * Returns: Empty body with one of the following:
     * ● 201 – in case of success
     * ● 204 – if the transaction is older than 30 seconds
     * ● 400 – if the JSON is invalid
     * ● 422 – if any of the fields are not parsable or the transaction date is in the future
     */
    public HttpStatus statistic(TransactionDTO transactionDTO){

        if (!NumberUtils.isParsable(transactionDTO.getAmount()) || !isDateValid(transactionDTO.getTimestamp())){
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }

        Statistic statistic = new Statistic();
        statistic.setAmount(BigDecimal.valueOf(Double.parseDouble(transactionDTO.getAmount())));

        OffsetDateTime odt = OffsetDateTime.parse( transactionDTO.getTimestamp() );
        LocalDateTime transactionTime = odt.toLocalDateTime();


        if(transactionTime.isAfter(LocalDateTime.now())){
            log.debug("is time in future is {}",transactionTime.isAfter(LocalDateTime.now()));
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }

        long transactionTimeDifference = ChronoUnit.SECONDS.between(odt.toLocalDateTime(),LocalDateTime.now());

        if(transactionTimeDifference > 30 ){
            log.debug("time dif is greater than 30 sec, time dif is {}",transactionTimeDifference);
            return HttpStatus.NO_CONTENT;
        }


        statistic.setTimestamp(Timestamp.from(odt.toInstant()));
        statistic.setId(getRandomNumber());

        statisticRepository.save(statistic);

        return HttpStatus.CREATED;
    }



    /**
     * TransactionResponse getStatistics()
     *
     * Returns:  TransactionResponse Object in the structure below
     * {"sum":"1000.00", "avg":"100.53", "max":"200000.49", "min":"50.23", "count":10}
     *
     * Where:
     * ● sum – a BigDecimal specifying the total sum of transaction value in the last 30 seconds
     * ● avg – a BigDecimal specifying the average amount of transaction value in the last 30 seconds
     * ● max – a BigDecimal specifying single highest transaction value in the last 30 seconds
     * ● min – a BigDecimal specifying single lowest transaction value in the last 30 seconds
     * ● count – a long specifying the total number of transactions that happened in the last 30 Seconds
     *
     *
     */
    public TransactionResponse getStatistics(){
        Collection<Statistic> all = statisticRepository.findAll();

        TransactionResponse response = new TransactionResponse();

        if(all.isEmpty()){
            return response;
        }

        List<BigDecimal> transactions = all.stream()
                .filter(c -> ChronoUnit.SECONDS.between(c.getTimestamp().toLocalDateTime(), LocalDateTime.now()) <= 30)
                .map(Statistic::getAmount)
                .sorted()
                .collect(Collectors.toList());

        BigDecimal sum = transactions.stream().reduce(BigDecimal.valueOf(0), BigDecimal::add);

        BigDecimal avg = sum.divide(BigDecimal.valueOf(transactions.size()), 2, RoundingMode.HALF_UP);

        response.setCount(transactions.size());
        response.setMin(transactions.get(0).setScale(2, RoundingMode.HALF_UP));
        response.setMax(transactions.get(transactions.size()-1).setScale(2, RoundingMode.HALF_UP));
        response.setSum(sum.setScale(2, RoundingMode.HALF_UP));
        response.setAvg(avg);

        return response;
    }


    /**
     *
     * @return void
     *
     * deletes all transactions added within the application life time
     */

    public HttpStatus delete(){
        statisticRepository.delete();

        return HttpStatus.NO_CONTENT;
    }


    private boolean isDateValid(String date){
        try {
            DateTimeFormatter.ISO_DATE_TIME.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            log.error("",e);
            return false;
        }

    }

    private int getRandomNumber() {
        return (int) ((Math.random() * (Integer.MAX_VALUE)) + 0);
    }

}
