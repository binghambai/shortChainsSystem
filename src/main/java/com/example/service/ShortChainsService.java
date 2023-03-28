package com.example.service;

import com.example.context.CommonErrorInfo;
import com.example.db.model.ShortUrl;
import com.example.db.respority.ShortUrlRepository;
import com.example.exceptions.RunningException;
import com.example.mq.producer.ShortChainsProducer;
import com.example.redisTemplate.RedisService;
import com.example.utils.SnowFlake;
import com.example.vo.BaseResponse;
import com.example.vo.ShortChainsRequest;
import com.example.vo.ShortChainsResponse;
import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShortChainsService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ShortChainsProducer shortChainsProducer;

    @Autowired
    private SnowFlake snowFlake;

    private static final String DOMAIN = "https://bh.com/";

    private static final int BLOOM_CYCLE_MAX_COUNT = 5;

    @Autowired
    private RedissonClient redissonClient;

    public BaseResponse<ShortChainsResponse> getShortChain(ShortChainsRequest request) {
        String shortUrl = getShortUri(request);

        String resUrl = DOMAIN + shortUrl;

        System.out.println("生成短链:" + resUrl);
        //生成短链
        return BaseResponse.success(ShortChainsResponse.builder().url(resUrl).build());
    }

    private synchronized String getShortUri(ShortChainsRequest request) {
        //获取redis数据
        String shortUrl = redisService.getString(request.getSourceUrl());
        Long resShortId = null;
        if (StringUtils.isBlank(shortUrl)) {
            //1 查询mysql中是否存在
            List<ShortUrl> dto = findUrl(request.getSourceUrl());
            if (dto == null || dto.size() != 1) {
                //2 mysql中不存在则生成
                resShortId = getShortId();

                // 3 判断生成的id是否存在歧义
                int curlCount = 0;
                while (redisService.bloomExist(resShortId.toString())) {
                    //需要重新生成一次
                    if (curlCount++ > BLOOM_CYCLE_MAX_COUNT) {
                        //查询一次db判断是否真的不存在
                        if (shortUrlRepository.existsById(resShortId)) {
                            resShortId = getShortId();
                        }
                        break;
                    }
                    resShortId = getShortId();
                }

                //4 存储到数据库db
                saveData(resShortId, request.getSourceUrl());
                //6 布隆列表
                redisService.addBloom(resShortId.toString());
            } else {
                //数据库存在就直接返回
                resShortId = dto.get(0).getId();
            }
            //5 存储到redis
            redisService.setString(request.getSourceUrl(), String.valueOf(resShortId), 60 * 60L);
        } else {
            return shortUrl;
        }
        return String.valueOf(resShortId);
    }

    private void saveData(Long shortId, String sourceUrl) {
        try {
            shortUrlRepository.save(ShortUrl.builder()
                    .id(shortId)
                    .sourceUr(sourceUrl)
                    .build());
        } catch (Exception e) {
            throw new RunningException(CommonErrorInfo.errorCode, "存储短链失败");
        }
    }

    private long getShortId() {
        return snowFlake.nextId();
    }

    private List<ShortUrl> findUrl(String sourceUrl) {
        return shortUrlRepository.findBySourceUr(sourceUrl);
    }
}
