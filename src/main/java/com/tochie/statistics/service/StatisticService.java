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


@Slf4j
@Service
public class StatisticService {

    @Autowired
    StatisticRepository statisticRepository;



    public HttpStatus statistic(TransactionDTO transactionDTO){

        if (!NumberUtils.isParsable(transactionDTO.getAmount()) || !isDateValid(transactionDTO.getTimestamp())){
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }

        Statistic statistic = new Statistic();
        statistic.setAmount(BigDecimal.valueOf(Double.parseDouble(transactionDTO.getAmount())));

        OffsetDateTime odt = OffsetDateTime.parse( transactionDTO.getTimestamp() );
        LocalDateTime transactionTime = odt.toLocalDateTime();

        if(transactionTime.isAfter(LocalDateTime.now())){
            log.error("is time in future is {}",transactionTime.isAfter(LocalDateTime.now()));
            return HttpStatus.UNPROCESSABLE_ENTITY;
        }

        long dif = ChronoUnit.SECONDS.between(odt.toLocalDateTime(),LocalDateTime.now());

        if(dif > 30 ){
            log.error("time dif is greater than 30 sec, time dif is {}",dif);
            return HttpStatus.NO_CONTENT;
        }


        statistic.setTimestamp(Timestamp.from(odt.toInstant()));
        statistic.setId(getRandomNumber());

        statisticRepository.save(statistic);

        return HttpStatus.ACCEPTED;
    }


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
