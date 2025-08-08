package com.dms.backend.controller;

import com.dms.backend.dto.CreateCaseRequest;
import com.dms.backend.model.Case;
import com.dms.backend.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseService caseService;

    @PostMapping
    public ResponseEntity<Case> createCase(@RequestBody CreateCaseRequest request) {
        Case createdCase = caseService.createCase(request.getName());
        return ResponseEntity.ok(createdCase);
    }
}
