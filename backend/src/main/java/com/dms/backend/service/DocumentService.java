package com.dms.backend.service;

import com.dms.backend.config.multitenancy.TenantContext;
import com.dms.backend.dto.DocumentIndexDto;
import com.dms.backend.model.Case;
import com.dms.backend.model.Document;
import com.dms.backend.repository.CaseRepository;
import com.dms.backend.repository.DocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dms.backend.dto.DocumentResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private CaseRepository caseRepository;
    @Autowired
    private MinioService minioService;
    @Autowired
    private TikaService tikaService;
    @Autowired
    private SearchService searchService;

    @Transactional
    public Document saveDocument(MultipartFile file, Long caseId) {
        // 1. Find the case (this will be filtered by tenant automatically)
        Case aCase = caseRepository.findById(caseId)
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + caseId));

        // 2. Upload file to Minio
        String storagePath = minioService.uploadFile(file);

        // 3. Save document metadata to DB
        Document doc = new Document();
        doc.setACase(aCase);
        doc.setName(file.getOriginalFilename());
        doc.setOriginalFilename(file.getOriginalFilename());
        doc.setContentType(file.getContentType());
        doc.setSize(file.getSize());
        doc.setStoragePath(storagePath);
        doc.setTenantId(TenantContext.getCurrentTenant());
        Document savedDoc = documentRepository.save(doc);

        // 4. Extract text and index
        try {
            String content = tikaService.extractText(file.getInputStream());
            DocumentIndexDto dto = new DocumentIndexDto(
                    savedDoc.getId().toString(),
                    savedDoc.getTenantId(),
                    content,
                    savedDoc.getName()
            );
            searchService.indexDocument(dto);
        } catch (Exception e) {
            // In a real app, handle this more gracefully (e.g., queue for re-indexing)
            throw new RuntimeException("Error processing file for indexing: " + e.getMessage(), e);
        }

        return savedDoc;
    }

    public List<Document> searchDocuments(String query) {
        String tenantId = TenantContext.getCurrentTenant();
        if (tenantId == null) {
            // Or throw an exception, depending on desired behavior for missing tenant
            return List.of();
        }

        // 1. Get document IDs from search service
        List<String> documentIds = searchService.searchDocuments(tenantId, query).block(); // Block to get result

        if (documentIds == null || documentIds.isEmpty()) {
            return List.of();
        }

        // 2. Convert IDs to Long
        List<Long> longIds = documentIds.stream()
                                        .map(Long::parseLong)
                                        .collect(Collectors.toList());

        // 3. Fetch full documents from the database
        return documentRepository.findAllById(longIds);
    }

    public DocumentResource downloadDocument(Long id) {
        // This lookup is tenant-aware due to the Hibernate filter
        Document doc = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + id));

        InputStream inputStream = minioService.getFile(doc.getStoragePath());

        return new DocumentResource(inputStream, doc.getContentType(), doc.getOriginalFilename());
    }
}
