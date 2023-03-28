package com.example.mq.consumer;

import com.example.mq.MessageAddress;
import com.example.service.ShortChainsStatisticsService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RocketMQMessageListener(topic = MessageAddress.TOPIC_BEGIN_STATISTIC_HOT_KEY, consumerGroup = "short-consumer-group")
public class BeginStatisticConsumer implements RocketMQListener<String> {

    @Autowired
    private ShortChainsStatisticsService shortChainsStatisticsService;

    private Map<String, Long> map = new ConcurrentHashMap<>();

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void onMessage(String message) {
        if (!map.containsKey(message)) {
            map.put(message, 1L);
        } else {
            map.put(message, map.get(message) + 1);
        }
    }

    public Map<String, Long> getMap() {
        return this.map;
    }
}
