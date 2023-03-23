package com.example.service;

import com.example.context.CommonErrorInfo;
import com.example.exceptions.RunningException;
import com.example.model.ShortUrl;
import com.example.redisTemplate.RedisService;
import com.example.redisTemplate.RedissonBloom;
import com.example.respority.ShortUrlRepository;
import com.example.utils.SnowFlake;
import com.example.vo.BaseResponse;
import com.example.vo.ShortChainsRequest;
import com.example.vo.ShortChainsResponse;
import io.micrometer.core.instrument.util.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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

    @Autowired
    private RedissonClient redissonClient;

    public BaseResponse<ShortChainsResponse> getShortChain(ShortChainsRequest request) {
        String shortUrl = getShortUri(request);

        String resUrl = DOMAIN + shortUrl;

        System.out.println("生成短链:" + resUrl);
        //生成短链
        return BaseResponse.success(ShortChainsResponse.builder().url(resUrl).build());
    }

    //TODO 需要修改布隆判断的位置，布隆中存储的应该是生成的shortId，校验这个id是否重复
    private String getShortUri(ShortChainsRequest request) {
        //布隆过滤器判断是否存在
        if (redisService.bloomExist(request.getSourceUrl())) {
            String shortUrl = redisService.getString(request.getSourceUrl());
            if (StringUtils.isNotBlank(shortUrl)) {
                //TODO 缓存中存在需要对当前的url进行判断是否是热门，对缓存进行续期，比如小时内访问次数超过20次
                return shortUrl;
            } else {
                //很小几率 需要查询数据库是否存在
                ShortUrl dto = findUrl(request.getSourceUrl());
                if (dto != null) {
                    return dto.getId().toString();
                }
            }
        }
        //缓存中不存在需要生成
        long shortId = getShortId();
        //存入redis
        redisService.setString(request.getSourceUrl(), String.valueOf(shortId), 60*60L);
        //写人布隆列表
        redisService.addBloom(request.getSourceUrl());
        //存储到数据库db
        saveData(shortId, request.getSourceUrl());
        return String.valueOf(shortId);
    }

    private Long saveData(Long shortId, String sourceUrl) {
        try {
            shortUrlRepository.save(ShortUrl.builder()
                            .id(shortId)
                            .sourceUr(sourceUrl)
                    .build());
        } catch (Exception e) {
            throw new RunningException(CommonErrorInfo.errorCode, "存储短链失败");
        }
        return shortId;
    }

    private long getShortId() {
        return snowFlake.nextId();
    }

    private ShortUrl findUrl(String sourceUrl) {
        return shortUrlRepository.findBySourceUr(sourceUrl);
    }
}
