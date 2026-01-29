package com.vara.controller;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.vara.dto.CreatePasteRequest;
import com.vara.entity.Paste;
import com.vara.repository.PasteRepository;
import com.vara.service.PasteService;
import com.vara.util.TimeProvider;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/* 2️ POST /api/pastes
   3️ GET /api/pastes/{id} */
@RestController
public class PasteApiController {

    private final PasteRepository pasteRepository;
    private final PasteService pasteService;
    private final TimeProvider timeProvider;

    public PasteApiController(PasteRepository pasteRepository,
                              PasteService pasteService,
                              TimeProvider timeProvider) {
        this.pasteRepository = pasteRepository;
        this.pasteService = pasteService;
        this.timeProvider = timeProvider;
    }

    @PostMapping("/api/pastes")
    public ResponseEntity<?> createPaste(@RequestBody CreatePasteRequest requestBody,
                                         HttpServletRequest request) {

        if (requestBody.getContent() == null || requestBody.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "content is required"));
        }

        if (requestBody.getTtl_seconds() != null && requestBody.getTtl_seconds() < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "ttl_seconds must be >= 1"));
        }

        if (requestBody.getMax_views() != null && requestBody.getMax_views() < 1) {
            return ResponseEntity.badRequest().body(Map.of("error", "max_views must be >= 1"));
        }

        Instant now = timeProvider.now(request);

        Paste paste = new Paste();
        paste.setId(UUID.randomUUID().toString());
        paste.setContent(requestBody.getContent());
        paste.setCreatedAt(now);
        paste.setViewCount(0);
        paste.setMaxViews(requestBody.getMax_views());

        if (requestBody.getTtl_seconds() != null) {
            paste.setExpiresAt(now.plusSeconds(requestBody.getTtl_seconds()));
        }

        pasteRepository.save(paste);

        return ResponseEntity.ok(
                Map.of(
                        "id", paste.getId(),
                        "url", "/p/" + paste.getId()
                )
        );
    }

   @GetMapping("/api/pastes/{id}")
public ResponseEntity<?> getPaste(@PathVariable String id, HttpServletRequest request) {
    Instant now = timeProvider.now(request);
    Paste paste = pasteService.fetch(id, now);

    if (paste == null) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Map.of("error", "Paste not found or expired"));
    }

    Integer remainingViews = 0;
    if (paste.getMaxViews() != null) {
        remainingViews = paste.getMaxViews() - paste.getViewCount();
        if (remainingViews < 0){
           remainingViews = 0; // safety
           pasteRepository.deleteById(id);
        }
    }

    return ResponseEntity.ok(Map.of(
        "content", paste.getContent(),
        "expires_at", paste.getExpiresAt(),
        "view_count", paste.getViewCount(),
        "max_views", paste.getMaxViews(),
        "remaining_views", remainingViews
    ));
}
}
