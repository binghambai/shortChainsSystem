package com.example.mq.consumer;

import com.example.mq.MessageAddress;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RocketMQMessageListener(topic = MessageAddress.TOPIC_TEST, consumerGroup = "tesst-group")
public class TestConsumer implements RocketMQListener<String> {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void onMessage(String message){
        System.out.println("get test msg");
        count.incrementAndGet();
    }

    public Integer getCount() {
        return this.count.get();
    }
}
