package com.vara.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vara.entity.Paste;
import com.vara.exception.NotFoundException;
import com.vara.repository.PasteRepository;

@Service
public class PasteService {

    private final PasteRepository repository;

    public PasteService(PasteRepository repository) {
        this.repository = repository;
    }
    
    @Transactional
    public Paste fetch(String id, Instant now) {
    Paste paste = repository.findById(id).orElse(null);
    if (paste == null) return null;

    // TTL check
    if (paste.getExpiresAt() != null && now.isAfter(paste.getExpiresAt())) {
        return null;
    }

    // View count check
    if (paste.getMaxViews() != null && paste.getViewCount() >= paste.getMaxViews()) {
        return null;
    }

    // Increment view count
    paste.setViewCount(paste.getViewCount() + 1);
    repository.save(paste);

    return paste;
}


}
