package com.dms.backend.service;

import com.dms.backend.model.Document;
import com.dms.backend.model.Tenant;
import com.dms.backend.repository.DocumentRepository;
import com.dms.backend.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RetentionPolicyService {

    private static final Logger logger = LoggerFactory.getLogger(RetentionPolicyService.class);

    @Autowired
    private TenantRepository tenantRepository;
    @Autowired
    private DocumentRepository documentRepository;
    @Autowired
    private MinioService minioService;
    @Autowired
    private SearchService searchService;

    @Transactional
    public void cleanupExpiredDocuments() {
        logger.info("Starting expired document cleanup job.");
        List<Tenant> tenantsWithPolicy = tenantRepository.findByRetentionDaysIsNotNull();

        for (Tenant tenant : tenantsWithPolicy) {
            LocalDateTime cutoffDate = LocalDateTime.now().minusDays(tenant.getRetentionDays());
            logger.info("Processing tenant '{}' with retention policy of {} days. Cutoff date: {}", tenant.getName(), tenant.getRetentionDays(), cutoffDate);

            List<Document> expiredDocuments = documentRepository.findAllByTenantIdAndCreatedDateBefore(tenant.getTenantId(), cutoffDate);

            for (Document doc : expiredDocuments) {
                logger.warn("Deleting expired document id: {}, name: {}, tenant: {}", doc.getId(), doc.getName(), tenant.getName());
                // 1. Delete from Minio
                minioService.deleteFile(doc.getStoragePath());
                // 2. Delete from ZincSearch
                searchService.deleteDocument(String.valueOf(doc.getId()));
                // 3. Delete from DB
                documentRepository.delete(doc);
            }
            logger.info("Finished processing tenant '{}'. Deleted {} documents.", tenant.getName(), expiredDocuments.size());
        }
        logger.info("Expired document cleanup job finished.");
    }
}
