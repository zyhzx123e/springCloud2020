package com.cloud.kafka02.constroller;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

@RestController
public class KafkaAsyncProducerController {
    @Resource
    private KafkaTemplate<Integer,String> template;


    @RequestMapping(path = "send/async/{message}")
    public String send(@PathVariable String message){

        final ListenableFuture<SendResult<Integer,String>> future
                = template.send("topic-spring-01",0,1,message);

            future.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    System.out.println("Send message failed:"+throwable.getMessage());

                }

                @Override
                public void onSuccess(SendResult<Integer, String> integerStringSendResult) {

                    final RecordMetadata metadata= integerStringSendResult.getRecordMetadata();
                    System.out.println("Send message succeeded:"+metadata.topic()+"\t"+metadata.partition()
                            +"\t"+metadata.offset());

                }
            });


        return "success";
    }

}
