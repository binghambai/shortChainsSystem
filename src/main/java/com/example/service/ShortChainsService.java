package com.example.service;

import com.example.context.CommonErrorInfo;
import com.example.exceptions.RunningException;
import com.example.model.ShortUrl;
import com.example.redisTemplate.RedisService;
import com.example.respority.ShortUrlRepository;
import com.example.utils.SnowFlake;
import com.example.vo.BaseResponse;
import com.example.vo.ShortChainsRequest;
import com.example.vo.ShortChainsResponse;
import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortChainsService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Autowired
    private RedisService redisService;

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

    private String getShortUri(ShortChainsRequest request) {
        //二级缓存获取数据
        String shortUrl = redisService.getString(request.getSourceUrl());
        if (StringUtils.isNotBlank(shortUrl)) {
            //TODO 缓存中存在需要对当前的url进行判断是否是热门，
            // 对缓存进行续期，比如小时内访问次数超过20
            // 考虑使用二级缓存，先去查询java 缓存
            return shortUrl;
        } else {
            //很小几率 需要查询数据库是否存在
            ShortUrl dto = findUrl(request.getSourceUrl());
            if (dto != null) {
                return dto.getId().toString();
            }
        }

        //缓存中不存在需要生成
        Long shortId = getShortId();

        //循环生成判断，知道生成的数据不重复
        //设置循环次数，防止布隆误判产生死循环
        int curlCount = 0;
        while (redisService.bloomExist(request.getSourceUrl())) {
            //需要重新生成一次
            if (curlCount++ > BLOOM_CYCLE_MAX_COUNT) {
                //查询一次db判断是否真的不存在
                if (!shortUrlRepository.existsById(shortId)) {
                    break;
                } else {
                    shortId = getShortId();
                }
            }
            shortId = getShortId();
        }
        //写人布隆列表
        redisService.addBloom(shortId.toString());
        //存入redis
        redisService.setString(request.getSourceUrl(), String.valueOf(shortId), 60*60L);
        //存储到数据库db
        saveData(shortId, request.getSourceUrl());
        return String.valueOf(shortId);
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

    private ShortUrl findUrl(String sourceUrl) {
        return shortUrlRepository.findBySourceUr(sourceUrl);
    }
}
