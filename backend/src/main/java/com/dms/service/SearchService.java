package com.dms.service;

import java.util.List;

public interface SearchService {
    void indexDocument(String indexName, String documentId, String content);
    void deleteDocument(String indexName, String documentId);
    List<String> searchDocuments(String indexName, String query);
}
