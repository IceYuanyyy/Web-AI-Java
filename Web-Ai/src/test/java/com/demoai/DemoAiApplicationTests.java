package com.demoai;

import com.demoai.service.AIService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

//@SpringBootTest
class DemoAiApplicationTests {


    @Autowired
    private AIService aiService;

    @Test
    void testDeepSeekAPI() {
        String response = aiService.getAIAnswer("请用100字介绍南京秦淮河");
        System.out.println("测试响应：" + response);
        assertTrue(response.length() > 50);
    }

}