package com.example.demo.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.example.demo.config.AlipayConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AlipayService {

    private final AlipayClient alipayClient;
    private final AlipayConfig alipayConfig;

    /**
     * 生成支付表单
     */
    public String createPayForm(String outTradeNo, double totalAmount, String subject) throws AlipayApiException {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setReturnUrl(alipayConfig.getReturnUrl());
        request.setNotifyUrl(alipayConfig.getNotifyUrl());
        
        String bizContent = String.format("{\"out_trade_no\":\"%s\","
                + "\"total_amount\":\"%s\","
                + "\"subject\":\"%s\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}",
                outTradeNo, totalAmount, subject);
        request.setBizContent(bizContent);
        
        return alipayClient.pageExecute(request).getBody();
    }
    
    /**
     * 查询交易状态
     */
    public AlipayTradeQueryResponse queryTrade(String outTradeNo) throws AlipayApiException {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        request.setBizContent(String.format("{\"out_trade_no\":\"%s\"}", outTradeNo));
        return alipayClient.execute(request);
    }
    
    /**
     * 生成订单号
     */
    public String generateTradeNo() {
        return UUID.randomUUID().toString().replace("-", "");
    }
} 