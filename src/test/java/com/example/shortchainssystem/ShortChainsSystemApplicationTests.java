package com.example.shortchainssystem;

import com.example.ShortChainsSystemApplication;
import com.example.model.ShortUrl;
import com.example.respority.ShortUrlRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

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

}
