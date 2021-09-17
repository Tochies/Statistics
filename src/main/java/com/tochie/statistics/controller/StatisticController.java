package com.tochie.statistics.controller;

import com.tochie.statistics.dto.TransactionDTO;
import com.tochie.statistics.dto.TransactionResponse;
import com.tochie.statistics.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class StatisticController {

    @Autowired
    StatisticService statisticService;

    @PostMapping("/transactions")
    public ResponseEntity<HttpStatus> createStatistic(@RequestBody TransactionDTO transactionDTO){

        try {
            HttpStatus response =  statisticService.saveStatistics(transactionDTO);
            return new ResponseEntity<>(response);
        } catch (JsonParseException e){

            return  new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        } catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @ResponseBody
    @GetMapping("/statistics")
    public ResponseEntity<TransactionResponse> getStatistic(){

        try {
            TransactionResponse ans = statisticService.getStatistics();

            return new ResponseEntity<>(ans, HttpStatus.OK);
        } catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/transactions")
    public ResponseEntity<HttpStatus> deleteStatistics(){

        try {
            HttpStatus response =  statisticService.deleteStatistics();
            return new ResponseEntity<>(response);
        } catch (Exception e){
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/ping")
    public String test(){
        return "Service is up";
    }
}
