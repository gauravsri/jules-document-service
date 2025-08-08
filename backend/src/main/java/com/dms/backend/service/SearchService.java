package com.dms.backend.service;

import com.dms.backend.dto.DocumentIndexDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SearchService {
    void indexDocument(DocumentIndexDto document);
    Mono<List<String>> searchDocuments(String tenantId, String query);
}
