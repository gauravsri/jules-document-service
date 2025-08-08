package com.dms.backend.service;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
public class TikaService {

    private final Tika tika = new Tika();

    public String extractText(InputStream stream) {
        try {
            return tika.parseToString(stream);
        } catch (Exception e) {
            // In a real app, use a custom exception and better logging
            throw new RuntimeException("Error extracting text with Tika: " + e.getMessage(), e);
        }
    }
}
