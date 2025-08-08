package com.dms.backend.service;

import com.dms.backend.dto.DocumentIndexDto;

public interface SearchService {
    void indexDocument(DocumentIndexDto document);
}
