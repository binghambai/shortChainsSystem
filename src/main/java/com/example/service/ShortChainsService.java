package com.example.service;

import com.example.model.ShortUrl;
import com.example.respority.ShortUrlRepository;
import com.example.vo.BaseResponse;
import com.example.vo.ShortChainsRequest;
import com.example.vo.ShortChainsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShortChainsService {

    @Autowired
    private ShortUrlRepository shortUrlRepository;

    public BaseResponse<ShortChainsResponse> getShortChain(ShortChainsRequest request) {
        //TODO
        ShortUrl save = shortUrlRepository.save(ShortUrl.builder()
                .id(121212L)
                .sourceUr("https://github.com/respority/binghambai")
                .build());
        System.out.println(save);

        return BaseResponse.success(null);
    }
}
