package com.april.house.biz.service;

import com.april.house.biz.mapper.CityMapper;
import com.april.house.common.model.City;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    @Autowired
    private CityMapper cityMapper;

    public List<City> getAllCities() {
        return cityMapper.getAllCities();
    }
}
