package com.cloud.kafka02.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    @KafkaListener(topics = "topic-spring-01")
    public void onMessage(ConsumerRecord<Integer,String> record){

        System.out.println("Consumer received data: "+record.topic()+"\t"+record.partition()
                +"\t"+record.offset()
                +"\t"+record.key()  +"\t"+record.value());
    }
}
