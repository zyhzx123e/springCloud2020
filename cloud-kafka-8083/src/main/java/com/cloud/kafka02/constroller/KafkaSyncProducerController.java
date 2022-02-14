package com.cloud.kafka02.constroller;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RestController
public class KafkaSyncProducerController {

    @Resource
    private KafkaTemplate<Integer,String> template;


    @RequestMapping(path = "send/sync/{message}")
    public String send(@PathVariable String message){

        final ListenableFuture<SendResult<Integer,String>> future = template.send("topic-spring-01",0,0,message);

        try {
            final SendResult<Integer,String> result=future.get();
            final RecordMetadata metadata=result.getRecordMetadata();
            System.out.println(metadata.topic()+"\t"+metadata.partition()
                    +"\t"+metadata.offset());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return "success";
    }

}
