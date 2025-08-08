package com.dms.backend.service.impl;

import com.dms.backend.dto.DocumentIndexDto;
import com.dms.backend.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ZincSearchServiceImpl implements SearchService {

    private static final Logger logger = LoggerFactory.getLogger(ZincSearchServiceImpl.class);

    private final WebClient webClient;

    private static final String INDEX_NAME = "documents";

    @Autowired
    public ZincSearchServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void indexDocument(DocumentIndexDto document) {
        logger.info("Indexing document with id: {} for tenant: {}", document.getId(), document.getTenantId());

        // The path for indexing a document with a specific ID is /api/{index}/_doc/{id}
        String path = String.format("/api/%s/_doc/%s", INDEX_NAME, document.getId());

        webClient.put()
                .uri(path)
                .body(Mono.just(document), DocumentIndexDto.class)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> logger.error("Error indexing document {}: {}", document.getId(), error.getMessage()))
                .doOnSuccess(success -> logger.info("Successfully indexed document {}", document.getId()))
                .subscribe();
    }
}
