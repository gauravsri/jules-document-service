package com.dms.backend.dto.zinc;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ZincSearchResponse {
    private Hits hits;

    @Data
    public static class Hits {
        @JsonProperty("total")
        private Total total;
        @JsonProperty("hits")
        private List<Hit> hits;
    }

    @Data
    public static class Total {
        private int value;
    }

    @Data
    public static class Hit {
        @JsonProperty("_id")
        private String id;
        @JsonProperty("_source")
        private Map<String, Object> source;
    }
}
