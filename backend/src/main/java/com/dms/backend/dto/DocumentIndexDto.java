package com.dms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentIndexDto {
    private String id; // The document ID from our main database
    private String tenantId;
    private String content;
    private String name;
}
