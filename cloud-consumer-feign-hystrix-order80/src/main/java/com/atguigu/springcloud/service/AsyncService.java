package com.atguigu.springcloud.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    private static Logger log = LoggerFactory.getLogger(AsyncService.class);

    @Resource
    @Qualifier("RestTemplateConfigMainRestTemplate")
    private RestTemplate restTemplate;

//    @Bean
//    public RestTemplate restTemplate() {
//        return new RestTemplate();
//    }

    @Async("asyncExecutor")
    public CompletableFuture<String> getEmployeeName() throws InterruptedException
    {
        log.info("getEmployeeName starts");

        String employeeNameData = restTemplate.getForObject("http://localhost:8080/name", String.class);

        log.info("employeeNameData, {}", employeeNameData);
        Thread.sleep(1000L);    //Intentional delay
        log.info("employeeNameData completed");
        return CompletableFuture.completedFuture(employeeNameData);
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> getEmployeeAddress() throws InterruptedException
    {
        log.info("getEmployeeAddress starts");

        String employeeAddressData = restTemplate.getForObject("http://localhost:8080/address", String.class);

        log.info("employeeAddressData, {}", employeeAddressData);
        Thread.sleep(1000L);    //Intentional delay
        log.info("employeeAddressData completed");
        return CompletableFuture.completedFuture(employeeAddressData);
    }

    @Async("asyncExecutor")
    public CompletableFuture<String> getEmployeePhone() throws InterruptedException
    {
        log.info("getEmployeePhone starts");

        String employeePhoneData = restTemplate.getForObject("http://localhost:8080/phone", String.class);

        log.info("employeePhoneData, {}", employeePhoneData);
        Thread.sleep(1000L);    //Intentional delay
        log.info("employeePhoneData completed");
        return CompletableFuture.completedFuture(employeePhoneData);
    }
}