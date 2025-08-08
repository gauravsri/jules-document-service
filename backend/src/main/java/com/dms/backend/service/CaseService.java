package com.dms.backend.service;

import com.dms.backend.config.multitenancy.TenantContext;
import com.dms.backend.model.Case;
import com.dms.backend.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CaseService {

    @Autowired
    private CaseRepository caseRepository;

    public Case createCase(String name) {
        Case newCase = new Case();
        newCase.setName(name);
        // Set the tenant ID from the context
        newCase.setTenantId(TenantContext.getCurrentTenant());
        return caseRepository.save(newCase);
    }
}
