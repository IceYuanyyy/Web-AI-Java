package com.demoai.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class KnowledgeService {

    // 内存知识库（可替换为SQLite/Excel读取）
    private static final List<Knowledge> KNOWLEDGE_BASE = Arrays.asList(
            new Knowledge("当地特色美食", "传统美食包括：1. XX糕 2. XX面"),
            new Knowledge("主要节日", "重要节日：春节、三月三歌圩节")
    );

    public String searchKnowledge(String question) {
        return KNOWLEDGE_BASE.stream()
                .filter(k -> k.getQuestion().contains(question))
                .findFirst()
                .map(Knowledge::getAnswer)
                .orElse(null);
    }

    @Data
    @AllArgsConstructor
    private static class Knowledge {
        private String question;
        private String answer;
    }
}