package com.dms.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Profile({"local", "test"})
public class ZincSearchService implements SearchService {

    @Override
    public void indexDocument(String indexName, String documentId, String content) {
        // TODO: Implement actual indexing logic for ZincSearch
    }

    @Override
    public void deleteDocument(String indexName, String documentId) {
        // TODO: Implement actual deletion logic for ZincSearch
    }

    @Override
    public List<String> searchDocuments(String indexName, String query) {
        // TODO: Implement actual search logic for ZincSearch
        return Collections.emptyList();
    }
}
