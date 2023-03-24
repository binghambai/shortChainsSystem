package com.example.mq.producer;

import com.alibaba.fastjson.JSON;
import com.example.db.model.ShortUrl;
import com.example.mq.MessageAddress;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortChainsProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 适用对发送消息失败场景很敏感的情况
     * @param arg
     */
    public void sendAsyncMessage(String arg) {
        rocketMQTemplate.asyncSend("short-chains-topic", "Hello world ! msg : " + arg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("消息发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("消息发送失败");
            }
        });
    }

    public void sendShortIdMessage(ShortUrl shortUrl) {
        rocketMQTemplate.convertAndSend(MessageAddress.TOPIC_SHORT_ID_GENERATE, JSON.toJSONString(shortUrl));
        System.out.println("普通消息发送成功");
    }

    public void sendBeginStatisticMessage() {
        rocketMQTemplate.convertAndSend(MessageAddress.TOPIC_BEGIN_STATISTIC_HOT_KEY, "");
        System.out.println("普通消息发送成功");
    }

    public void sendOrderMessage(String msg) {
        System.out.println("get message");
        rocketMQTemplate.syncSendOrderly(MessageAddress.TOPIC_TEST, "test", MessageAddress.TOPIC_TEST);
    }
}
