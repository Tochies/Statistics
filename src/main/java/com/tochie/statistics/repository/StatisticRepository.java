package com.tochie.statistics.repository;


import com.tochie.statistics.model.Statistic;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Repository
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class StatisticRepository {

    private static Map<Integer, Statistic> statisticMap = new ConcurrentHashMap<>();

    public  void save(Statistic statistic){
        statisticMap.putIfAbsent(statistic.getId(), statistic);
    }


    public Collection<Statistic> findAll(){
        return statisticMap.values();
    }

    public void delete(){
        statisticMap.clear();
    }



}
