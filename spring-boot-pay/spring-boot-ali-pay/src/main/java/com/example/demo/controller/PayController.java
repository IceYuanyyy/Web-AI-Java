package com.example.demo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.example.demo.config.AlipayConfig;
import com.example.demo.service.AlipayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/pay")
@RequiredArgsConstructor
public class PayController {

    private final AlipayService alipayService;
    private final AlipayConfig alipayConfig;

    /**
     * 创建支付订单
     */
    @GetMapping("/create")
    public String createPay(@RequestParam(defaultValue = "0.01") Double amount,
                            //其中这里defaultValue调用sql数据库商品的价格
                            //测试商品defaultValue调用sql的数据库商品名称
                           @RequestParam(defaultValue = "测试商品") String subject) throws AlipayApiException {
        String outTradeNo = alipayService.generateTradeNo();
        String form = alipayService.createPayForm(outTradeNo, amount, subject);
        log.info("创建支付订单成功: {}", outTradeNo);
        return form;
    }

    /**
     * 支付宝同步回调
     */
    @GetMapping("/return")
    public String returnCallback(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = convertRequestParams(request);
        boolean signVerified = verifySign(params);
        
        if (signVerified) {
            String outTradeNo = params.get("out_trade_no");
            String tradeNo = params.get("trade_no");
            log.info("支付宝同步回调成功 - 商户订单号: {}, 支付宝交易号: {}", outTradeNo, tradeNo);
            return "支付成功";
        } else {
            return "签名验证失败";
        }
    }

    /**
     * 支付宝异步通知
     */
    @PostMapping("/notify")
    public String notifyCallback(HttpServletRequest request) throws AlipayApiException {
        Map<String, String> params = convertRequestParams(request);
        boolean signVerified = verifySign(params);
        
        if (signVerified) {
            String outTradeNo = params.get("out_trade_no");
            String tradeNo = params.get("trade_no");
            String tradeStatus = params.get("trade_status");
            
            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                // 支付成功，更新订单状态
                log.info("支付成功 - 商户订单号: {}, 支付宝交易号: {}", outTradeNo, tradeNo);
                return "success";
            }
        }
        
        return "failure";
    }
    
    /**
     * 将请求参数转换为Map
     */
    private Map<String, String> convertRequestParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }
        return params;
    }
    
    /**
     * 验证签名
     */
    private boolean verifySign(Map<String, String> params) throws AlipayApiException {
        return AlipaySignature.rsaCheckV1(
                params,
                alipayConfig.getAlipayPublicKey(),
                alipayConfig.getCharset(),
                alipayConfig.getSignType()
        );
    }
} 