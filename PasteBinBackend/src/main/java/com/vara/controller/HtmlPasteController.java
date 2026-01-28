package com.vara.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.vara.entity.Paste;
import com.vara.service.PasteService;

import java.time.Instant;
import java.util.Map;

@Controller
public class HtmlPasteController {

	private final PasteService pasteService;

	public HtmlPasteController(PasteService pasteService) {
		this.pasteService = pasteService;
	}

	// @GetMapping("/p/{id}")
	// public ResponseEntity<?> getPaste(@PathVariable String id) {
	//     Paste paste = pasteService.fetch(id, Instant.now());
	//     if (paste == null) {
	//         return ResponseEntity.status(HttpStatus.NOT_FOUND)
	//                              .body(Map.of("error", "Paste not found or expired"));
	//     }
	//     Integer remainingViews = 0;
 //        if (paste.getMaxViews() != null) {
 //            remainingViews = paste.getMaxViews() - paste.getViewCount();
 //            if (remainingViews < 0) remainingViews = 0; // safety
 //        }
	//     return ResponseEntity.ok(Map.of(
	//         "content", paste.getContent(),
	//         "expires_at", paste.getExpiresAt(),
	//         "view_count", paste.getViewCount(),
	//         "max_views", paste.getMaxViews(),
	//         "remaining_views", remainingViews
	//     ));
	// }
	@GetMapping("/p/{id}")
	@ResponseBody
	public ResponseEntity<String> getPaste(@PathVariable String id, HttpServletRequest request) {
    	Instant now = timeProvider.now(request);
    	Paste paste = pasteService.fetch(id, now);
    	if (paste == null) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body("<h1>Paste not found or expired</h1>");
    	}

    	// Escape content to prevent script injection
    	String safeContent = paste.getContent()
                              .replaceAll("<", "&lt;")
                              .replaceAll(">", "&gt;");

    	// Calculate remaining views
    	Integer remainingViews = 0;
    	if (paste.getMaxViews() != null) {
        	remainingViews = paste.getMaxViews() - paste.getViewCount();
        	if (remainingViews < 0) remainingViews = 0;
    	}

    	// Build HTML response
    	String html = "<html><body>" +
                  "<h2>Paste Content</h2>" +
                  "<pre>" + safeContent + "</pre>" +
                  "<p>View count: " + paste.getViewCount() + "</p>" +
                  "<p>Max views: " + (paste.getMaxViews() != null ? paste.getMaxViews() : "Unlimited") + "</p>" +
                  "<p>Remaining views: " + remainingViews + "</p>" +
                  "<p>Expires at: " + (paste.getExpiresAt() != null ? paste.getExpiresAt() : "Never") + "</p>" +
                  "</body></html>";

    	return ResponseEntity.ok(html);
	}
}
