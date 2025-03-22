package com.demoai.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class AIService {

    @Value("${kimi.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.moonshot.cn/v1/chat/completions";

    public String getAIAnswer(String question) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "moonshot-v1-8k");
        requestBody.put("temperature", 0.5);  // Kimi推荐较低temperature值
        requestBody.put("max_tokens", 1024);

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "user",
                "content", buildPrompt(question)  // 添加定制prompt
        ));
        requestBody.put("messages", messages);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    API_URL,
                    new HttpEntity<>(requestBody, headers),
                    Map.class
            );

            return parseKimiResponse(response.getBody());
        } catch (HttpClientErrorException e) {
            return "请求错误：" + e.getResponseBodyAsString();
        } catch (Exception e) {
            return "接口调用失败：" + e.getMessage();
        }
    }

    private String buildPrompt(String question) {
        return String.format("你是一个地方文化专家，回答需满足：\n"
                + "1. 使用口语化中文\n"
                + "2. 长度不超过200字\n"
                + "3. 包含具体案例\n"
                + "问题：%s", question);
    }

    private String parseKimiResponse(Map<String, Object> response) {
        if (response.containsKey("error")) {
            return "API错误：" + ((Map)response.get("error")).get("message");
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<String, String> message = (Map<String, String>) choices.get(0).get("message");
            return message.getOrDefault("content", "未获得有效回答");
        }
        return "响应解析失败";
    }
}