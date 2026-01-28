package com.vara.controller;

import org.springframework.web.bind.annotation.*;

import com.vara.repository.PasteRepository;

import java.util.Map;

/* 1️ GET /api/healthz */
@RestController
public class HealthController {

    private final PasteRepository pasteRepository;

    public HealthController(PasteRepository pasteRepository) {
        this.pasteRepository = pasteRepository;
    }

    @GetMapping("/api/healthz")
    public Map<String, Boolean> health() {
        pasteRepository.count(); // DB connectivity check
        return Map.of("ok", true);
    }
}