package com.example.shortchainssystem;

import com.example.ShortChainsSystemApplication;
import com.example.db.model.ShortUrl;
import com.example.db.model.ShortUrlHotDatum;
import com.example.db.respority.ShortUrlHotDatumRepository;
import com.example.redisTemplate.RedisService;
import com.example.db.respority.ShortUrlRepository;
import com.example.utils.SnowFlake;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@SpringBootTest(classes = {ShortChainsSystemApplication.class})
@RunWith(SpringRunner.class)
class ShortChainsSystemApplicationTests {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    @Test
    void contextLoads() {
        ShortUrl save = shortUrlRepository.save(ShortUrl.builder()
                .id(111111111L)
                .sourceUr("https://github.com/respority/binghambai")
                .build());
        System.out.println(save);
    }

    @Autowired
    private RedisService redisService;

    @Test
    void testRedis() {
        redisService.setString("test", "test112313", 50L);

        System.out.println(redisService.getString("test"));
    }

    @Autowired
    private RedissonClient redissonClient;

    @Test
    void testRedisBloom() {
        RBloomFilter<String> filter = redissonClient.getBloomFilter("sample");
        // 初始大小   可接受的错误率
        filter.tryInit(55000L, 0.03);


        System.out.println(filter.contains("test1"));
        System.out.println(filter.contains("test2_test"));
        System.out.println(filter.contains("test_json"));
        System.out.println(filter.count());

    }

    @Test
    public void existById() {
        System.out.println(shortUrlRepository.existsById(1123123123L));
    }

    @Autowired
    private SnowFlake snowFlake;

    @Test
    void testSnowFlak() {
        long id = snowFlake.nextId();
        System.out.println(id);
    }

    @Autowired
    private ShortUrlHotDatumRepository shortUrlHotDatumRepository;

    @Test
    void testJpa() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0)
                .minus(1, ChronoUnit.DAYS);
        LocalDateTime end = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        List<ShortUrlHotDatum> byRangeTime = shortUrlHotDatumRepository.findByRangeTime(start, end);
        System.out.println(byRangeTime);
    }

}
