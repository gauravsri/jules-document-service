package com.dms.backend.controller;

import com.dms.backend.model.Document;
import com.dms.backend.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping
    public ResponseEntity<Document> uploadDocument(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("caseId") Long caseId) {
        Document savedDocument = documentService.saveDocument(file, caseId);
        return ResponseEntity.ok(savedDocument);
    }
}
