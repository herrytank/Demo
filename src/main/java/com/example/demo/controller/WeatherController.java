package com.example.demo.controller;

import com.example.demo.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    @GetMapping(path ="/rest/getTempature")
    public ResponseEntity<HashMap> getTempature(String province, String city, String county){
        Optional<List<String>> temp = weatherService.getTemparature(province,city,county);
        return new ResponseEntity(temp, HttpStatus.OK);
    }
}
