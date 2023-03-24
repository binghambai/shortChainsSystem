package com.example.controller;

import com.example.mq.consumer.TestConsumer;
import com.example.mq.producer.ShortChainsProducer;
import com.example.service.ShortChainsService;
import com.example.vo.BaseResponse;
import com.example.vo.ShortChainsRequest;
import com.example.vo.ShortChainsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ShortChainsController {

    @Autowired
    private ShortChainsService shortChainsService;

    @PostMapping("/api/getShortChain")
    public BaseResponse<ShortChainsResponse> getShortChain(@RequestBody ShortChainsRequest request) {
        return shortChainsService.getShortChain(request);
    }


    @Autowired
    private ShortChainsProducer shortChainsProducer;

    @GetMapping("/api/test/order/mq")
    public BaseResponse<ShortChainsResponse> getShortChai(@RequestParam(value = "msg") String request) {
        shortChainsProducer.sendOrderMessage("");
        return null;
    }

    @Autowired
    private TestConsumer testConsumer;

    @GetMapping("/api/test/order/get")
    public BaseResponse<ShortChainsResponse> get() {
        System.out.println(testConsumer.getCount());
        return null;
    }
}
