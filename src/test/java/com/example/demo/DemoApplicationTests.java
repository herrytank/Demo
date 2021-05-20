package com.example.demo;

import com.example.demo.service.WeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RunWith(SpringRunner.class)
@SpringBootTest
class DemoApplicationTests implements  Runnable{

    private static final String URL = "http://127.0.0.1/rest/getTempature";

    @Test
    void contextLoads() {
        ExecutorService pool = Executors.newCachedThreadPool();
        for(int i=0;i<20;i++){
            pool.execute(new DemoApplicationTests());
        }
    }

    @Test
    void testPositive(){
        //test positive
        WeatherService weather = new WeatherService();
        Optional<List<String>> requestResult1 = weather.getTemparature("广东","广州","从化");

        List<String> listStr1 = new ArrayList<>();
        listStr1.add("25.6");
        Optional<List<String>> expectResult1 = Optional.ofNullable(listStr1);

        Assertions.assertEquals(requestResult1,expectResult1);
    }

    @Test
    void testInvalidProvince(){
        //test invaid for province
        WeatherService weather1 = new WeatherService();
        Optional<List<String>> requestResult2 = weather1.getTemparature("广汽","广州","从化");

        List<String> listStr2 = new ArrayList<>();
        listStr2.add("北京");
        listStr2.add("上海");
        listStr2.add("天津");
        listStr2.add("重庆");
        listStr2.add("黑龙江");
        listStr2.add("吉林");
        listStr2.add("辽宁");
        listStr2.add("内蒙古");
        listStr2.add("河北");
        listStr2.add("山西");
        listStr2.add("陕西");
        listStr2.add("山东");
        listStr2.add("新疆");
        listStr2.add("西藏");
        listStr2.add("青海");
        listStr2.add("甘肃");
        listStr2.add("宁夏");
        listStr2.add("河南");
        listStr2.add("江苏");
        listStr2.add("湖北");
        listStr2.add("浙江");
        listStr2.add("安徽");
        listStr2.add("福建");
        listStr2.add("江西");
        listStr2.add("湖南");
        listStr2.add("贵州");
        listStr2.add("四川");
        listStr2.add("广东");
        listStr2.add("云南");
        listStr2.add("广西");
        listStr2.add("海南");
        listStr2.add("香港");
        listStr2.add("澳门");
        listStr2.add("台湾");

        Optional<List<String>> expectResult2 = Optional.ofNullable(listStr2);

        Assertions.assertEquals(requestResult2,expectResult2);
    }

    @Test
    void testInvalidCity(){
    //test invalid city
        WeatherService weather1 = new WeatherService();
        Optional<List<String>> requestResult2 = weather1.getTemparature("广东","西凉","从化");

        List<String> listStr2 = new ArrayList<>();
        listStr2.add("广州");
        listStr2.add("韶关");
        listStr2.add("惠州");
        listStr2.add("梅州");
        listStr2.add("汕头");
        listStr2.add("深圳");
        listStr2.add("珠海");
        listStr2.add("佛山");
        listStr2.add("肇庆");
        listStr2.add("湛江");
        listStr2.add("江门");
        listStr2.add("河源");
        listStr2.add("清远");
        listStr2.add("云浮");
        listStr2.add("潮州");
        listStr2.add("东莞");
        listStr2.add("中山");
        listStr2.add("阳江");
        listStr2.add("揭阳");
        listStr2.add("茂名");
        listStr2.add("汕尾");

        Optional<List<String>> expectResult2 = Optional.ofNullable(listStr2);

        Assertions.assertEquals(requestResult2,expectResult2);

    }

    @Test
    void testInvalidCounty(){
        //test invalid county
        WeatherService weather = new WeatherService();
        Optional<List<String>> requestResult1 = weather.getTemparature("广东","广州","河西");

        List<String> listStr1 = new ArrayList<>();
        listStr1.add("广州");
        listStr1.add("番禺");
        listStr1.add("从化");
        listStr1.add("增城");
        listStr1.add("花都");
        Optional<List<String>> expectResult1 = Optional.ofNullable(listStr1);

        Assertions.assertEquals(requestResult1,expectResult1);
    }

    @Override
    public void run() {
        try {
            String reqBody = setRequestBody(getHttpParamStr());
            HttpResponse<String> resp = Unirest.post(URL.toString())
                    .header("Content-Type", "application/json")
                    .body(reqBody)
                    .asString();
            System.out.println("Request result:"+resp);
        } catch (UnirestException | JsonProcessingException unirestException) {
            unirestException.printStackTrace();
        }
    }

    private String setRequestBody(Map<String,Object> paramMap) throws JsonProcessingException {
        com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper();
        String reqBody = objectMapper.writeValueAsString(paramMap);
        return reqBody;
    }

    public  static Map<String,Object> getHttpParamStr() {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("province", "广东");
        param.put("city", "广州");
        param.put("city", "从化");
        return param;
    }



}
