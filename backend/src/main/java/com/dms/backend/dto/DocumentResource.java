package com.dms.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.io.InputStream;

@Getter
@AllArgsConstructor
public class DocumentResource {
    private final InputStream inputStream;
    private final String contentType;
    private final String filename;
}
