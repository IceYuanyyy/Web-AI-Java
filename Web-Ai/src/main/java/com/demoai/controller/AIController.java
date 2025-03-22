package com.demoai.controller;

import com.demoai.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> handleQuestion(@RequestBody Map<String, String> request) {
        String question = request.get("question");

        // 1. 优先从本地知识库获取答案 todo
        //String localAnswer = aiService.getLocalAnswer(question);
        String localAnswer=null;

        // 2. 本地无匹配时调用AI接口
        if(localAnswer == null) {
            String aiResponse = aiService.getAIAnswer(question);
            return ResponseEntity.ok(Map.of("answer", aiResponse, "source", "AI生成"));
        }

        return ResponseEntity.ok(Map.of("answer", localAnswer, "source", "本地知识库"));
    }
}