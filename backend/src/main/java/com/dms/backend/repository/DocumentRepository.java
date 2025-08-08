package com.dms.backend.repository;

import com.dms.backend.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByTenantIdAndCreatedDateBefore(String tenantId, LocalDateTime cutoffDate);
}
