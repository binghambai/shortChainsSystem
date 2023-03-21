package com.example.controller;

import com.example.service.ShortChainsService;
import com.example.vo.BaseResponse;
import com.example.vo.ShortChainsRequest;
import com.example.vo.ShortChainsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShortChainsController {

    @Autowired
    private ShortChainsService shortChainsService;

    @PostMapping("/api/getShortChain")
    public BaseResponse<ShortChainsResponse> getShortChain(@RequestBody ShortChainsRequest request) {
        return shortChainsService.getShortChain(request);
    }
}
