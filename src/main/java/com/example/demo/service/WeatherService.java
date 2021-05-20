package com.example.demo.service;

import com.example.demo.model.Weather;
import com.example.demo.util.HttpClientUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeatherService {
    private static final String BASE_URL = "http://www.weather.com.cn/data/";

    @Autowired
    HttpClientUtil httpClientUtil;

    @Async("asyncServiceExecutor")
    public Optional<List<String>> getTemparature(String province, String city, String county){
        Map<String, String> provincesMap = getProvince();
        Optional<List<String>> tmp = null;


        //check if the province is valid
        if(provincesMap.values().contains(province)){
            List<String> provinceCode = provincesMap.entrySet().stream().filter(kvEntry -> Objects.equals(kvEntry.getValue(), province))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            Map<String, String> citiesMap = getCity(provinceCode.get(0));
            //check if the city is valid
            if(citiesMap.values().contains(city)){
                List<String> cityCode = citiesMap.entrySet().stream().filter(kvEntry -> Objects.equals(kvEntry.getValue(), city))
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());
                Map<String, String> countiesMap = getCounty(provinceCode.get(0),cityCode.get(0));


                //check if the county is valid
                if(countiesMap.values().contains(county)){
                    List<String> countyCode = countiesMap.entrySet().stream().filter(kvEntry -> Objects.equals(kvEntry.getValue(), county))
                            .map(Map.Entry::getKey)
                            .collect(Collectors.toList());
                    tmp = getTempatureForCertainCounty(provinceCode.get(0), cityCode.get(0), countyCode.get(0));
                }else{
                    //if the county is not valid, return valid county
                    List<String> countyList = countiesMap.entrySet().stream()
                            .map(Map.Entry::getValue)
                            .collect(Collectors.toList());
                    tmp = Optional.ofNullable(countyList);
                    System.out.println(tmp);
                }
            }else{
                //if the city is not valid, return valid city
                List<String> cityList = citiesMap.entrySet().stream()
                        .map(Map.Entry::getValue)
                        .collect(Collectors.toList());
                tmp = Optional.ofNullable(cityList);
                System.out.println(tmp);
            }
        }else{
            //if the province is not valid, return valid province
            List<String> provinceList = provincesMap.entrySet().stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
            tmp = Optional.ofNullable(provinceList);
            System.out.println(tmp);
        }
        return tmp;
    }

    private Map<String,String> getProvince(){
        Gson gson = new Gson();
        String resultStr = HttpClientUtil.doGet(BASE_URL+"/city3jdata/china.html",null);
        Map<String,String> resultMap = gson.fromJson(resultStr,Map.class);

        return resultMap;
    }

    private Map<String,String> getCity(String provinceCode){
        Gson gson = new Gson();
        String resultStr = HttpClientUtil.doGet(BASE_URL+"/city3jdata/provshi/"+provinceCode+".html",null);
        Map<String,String> resultMap = gson.fromJson(resultStr,Map.class);

        return resultMap;
    }

    private Map<String,String> getCounty(String provinceCode,String cityCode){
        Gson gson = new Gson();
        String resultStr = HttpClientUtil.doGet(BASE_URL+"/city3jdata/station/"+provinceCode+cityCode+".html",null);
        Map<String,String> resultMap = gson.fromJson(resultStr,Map.class);

        return resultMap;
    }

    private Optional<List<String>> getTempatureForCertainCounty(String provinceCode, String cityCode, String countyCode){

        String resultStr = HttpClientUtil.doGet(BASE_URL+"/sk/"+provinceCode+cityCode+countyCode+".html",null);

        Gson gson = new Gson();
        Map<String,String> resultMap = gson.fromJson(resultStr,Map.class);
        Optional<List<String>> tempature = null;

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        String jsonString = new Gson().toJson(resultMap.get("weatherinfo"), Map.class);
        System.out.println(jsonString);
        if(jsonString!=null&& jsonString!=""){
            Map<String,String> weatherMap = null;
            try {
                weatherMap = objectMapper.readValue(jsonString, Map.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            List<String> weatherList = weatherMap.entrySet().stream().filter(kvEntry -> Objects.equals(kvEntry.getKey(), "temp")).map(Map.Entry::getValue)
                    .collect(Collectors.toList());

            tempature = Optional.ofNullable(weatherList);
                System.out.println(tempature);
        }

        return tempature;
    }
}
