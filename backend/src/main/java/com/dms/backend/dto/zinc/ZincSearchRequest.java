package com.dms.backend.dto.zinc;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZincSearchRequest {
    private String search_type;
    private Query query;
    private int from;
    private int max_results;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Query {
        private String term;
        private Map<String, Float> fields;
    }
}
