package com.dms.backend.tasks;

import com.dms.backend.service.RetentionPolicyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

    @Autowired
    private RetentionPolicyService retentionPolicyService;

    // Run once every day at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void runCleanupExpiredDocuments() {
        retentionPolicyService.cleanupExpiredDocuments();
    }
}
