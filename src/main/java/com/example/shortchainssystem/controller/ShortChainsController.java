package com.example.shortchainssystem.controller;

import com.example.shortchainssystem.service.ShortChainsService;
import com.example.shortchainssystem.vo.BaseResponse;
import com.example.shortchainssystem.vo.ShortChainsRequest;
import com.example.shortchainssystem.vo.ShortChainsResponse;
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
