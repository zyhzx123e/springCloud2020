package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.service.PaymentHystrixService;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @auther zzyy
 * @create 2020-02-20 11:57
 */
@RestController
@Slf4j
@DefaultProperties(defaultFallback = "payment_Global_FallbackMethod")
public class OrderHystirxController
{
    @Resource
    private PaymentHystrixService paymentHystrixService;

    @PostMapping("/upload")
    public String upload(@RequestPart("files") MultipartFile [] files) throws IOException {

        //config application.yml:
        //spring.servlet.multipart.max-file-size=1MB //Defalt 1MB
        //spring.servlet.multipart.max-request-size=10MB //Defalt 10MB
        int fP=files.length;
        while(--fP>=0){
            if(files[fP].isEmpty())continue;

            long size=files[fP].getSize();
            String contentType = files[fP].getContentType();
            String name = files[fP].getName();
            //InputStream inputStream = files[fP].getInputStream();

            log.info("file size : {}, \ncontentType : {}, \nname: {}",size,contentType,name);
            files[fP].transferTo(new File("D:\\testtomcat\\"+name));
        }
        return "upload";//thymeleaf
    }

    @GetMapping("/consumer/payment/hystrix/ok/{id}")
    public String paymentInfo_OK(@PathVariable("id") Integer id)
    {
        String result = paymentHystrixService.paymentInfo_OK(id);
        return result;
    }

    @GetMapping("/consumer/payment/hystrix/timeout/{id}")
    @HystrixCommand(fallbackMethod = "paymentTimeOutFallbackMethod",commandProperties = {
            @HystrixProperty(name="execution.isolation.thread.timeoutInMilliseconds",value="1500")
    })
    //@HystrixCommand
    public String paymentInfo_TimeOut(@PathVariable("id") Integer id)
    {
        int age = 10/0;
        String result = paymentHystrixService.paymentInfo_TimeOut(id);
        return result;
    }
    public String paymentTimeOutFallbackMethod(@PathVariable("id") Integer id)
    {
        return "我是消费者80,对方支付系统繁忙请10秒钟后再试或者自己运行出错请检查自己,o(╥﹏╥)o";
    }

    // 下面是全局fallback方法
    public String payment_Global_FallbackMethod()
    {
        return "Global异常处理信息，请稍后再试，/(ㄒoㄒ)/~~";
    }
}
