package com.dms.backend.service.impl;

import com.dms.backend.dto.DocumentIndexDto;
import com.dms.backend.dto.zinc.ZincSearchResponse;
import com.dms.backend.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @Override
    public Mono<List<String>> searchDocuments(String tenantId, String query) {
        String searchPath = String.format("/api/%s/_search", INDEX_NAME);

        Map<String, Object> queryBody = Map.of(
            "query", Map.of(
                "bool", Map.of(
                    "must", List.of(
                        Map.of("match", Map.of("content", query))
                    ),
                    "filter", List.of(
                        // Assuming tenantId is mapped as a keyword field for exact matching
                        Map.of("term", Map.of("tenantId.keyword", tenantId))
                    )
                )
            ),
            "from", 0,
            "max_results", 20,
            "_source", List.of() // We only need the IDs, not the full document source
        );

        return webClient.post()
                .uri(searchPath)
                .bodyValue(queryBody)
                .retrieve()
                .bodyToMono(ZincSearchResponse.class)
                .map(response -> response.getHits().getHits().stream()
                        .map(ZincSearchResponse.Hit::getId)
                        .collect(Collectors.toList()))
                .doOnError(error -> logger.error("Error searching documents for tenant {}: {}", tenantId, error.getMessage()));
    }

    @Override
    public void deleteDocument(String documentId) {
        logger.info("Deleting document from index with id: {}", documentId);
        String deletePath = String.format("/api/%s/_doc/%s", INDEX_NAME, documentId);

        webClient.delete()
                .uri(deletePath)
                .retrieve()
                .bodyToMono(Void.class)
                .doOnError(error -> logger.error("Error deleting document {} from index: {}", documentId, error.getMessage()))
                .doOnSuccess(success -> logger.info("Successfully deleted document {} from index", documentId))
                .subscribe();
    }
}
