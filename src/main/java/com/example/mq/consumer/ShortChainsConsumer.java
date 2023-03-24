package com.example.mq.consumer;

import com.alibaba.fastjson.JSONObject;
import com.example.db.model.ShortUrl;
import com.example.mq.MessageAddress;
import com.example.service.ShortChainsStatisticsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = MessageAddress.TOPIC_SHORT_ID_GENERATE, consumerGroup = "short-consumer-group")
public class ShortChainsConsumer implements RocketMQListener<String> {

    @Autowired
    private ShortChainsStatisticsService shortChainsStatisticsService;

    @Override
    public void onMessage(String message) {
        System.out.println("get source json message:" + message);
        ShortUrl shortUrl = JSONObject.parseObject(message, ShortUrl.class);
        shortChainsStatisticsService.statisticsShortUrl(shortUrl);
    }
}
