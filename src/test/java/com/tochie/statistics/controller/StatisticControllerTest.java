package com.tochie.statistics.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tochie.statistics.dto.TransactionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class StatisticControllerTest {

    private MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }


    @Test
    void createStatisticSuccess() throws Exception {

        mvc.perform( MockMvcRequestBuilders
                .post("/transactions")
                .content(asJsonString(transactionDTO()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void getStatisticFailed() throws Exception {

        mvc.perform( MockMvcRequestBuilders
                .get("/statistics")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").isEmpty());

    }

    @Test
    void getStatisticSuccess() throws Exception {

        mvc.perform( MockMvcRequestBuilders
                .post("/transactions")
                .content(asJsonString(transactionDTO()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        mvc.perform( MockMvcRequestBuilders
                .get("/statistics")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.count").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.sum").isNotEmpty());
    }


    @Test
    void deleteStatistics() throws Exception {

        mvc.perform( MockMvcRequestBuilders.delete("/transactions") )
                .andExpect(status().isNoContent());
    }


    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static TransactionDTO transactionDTO(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date date = new Date();

        TransactionDTO transactionDTO = new TransactionDTO() ;
        transactionDTO.setAmount("168.090");
        transactionDTO.setTimestamp(sdf.format(date));

        return transactionDTO;
    }

}