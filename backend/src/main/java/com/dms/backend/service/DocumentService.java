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
import org.springframework.web.multipart.MultipartFile;

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
}
