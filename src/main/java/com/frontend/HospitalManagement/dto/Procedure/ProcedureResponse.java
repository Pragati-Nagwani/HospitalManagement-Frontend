package com.frontend.HospitalManagement.dto.Procedure;

import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcedureResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @JsonProperty("_links")
    private Map<String, Object> links;

    private PageMetadata page;

    public Embedded getEmbedded() { return embedded; }
    public void setEmbedded(Embedded embedded) { this.embedded = embedded; }

    public PageMetadata getPage() { return page; }
    public void setPage(PageMetadata page) { this.page = page; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        private List<ProcedureDto> procedures;

        public List<ProcedureDto> getProcedures() { return procedures; }
        public void setProcedures(List<ProcedureDto> procedures) { this.procedures = procedures; }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PageMetadata {
        private int size;
        private long totalElements;
        private int totalPages;
        private int number;

        public int getSize() { return size; }
        public void setSize(int size) { this.size = size; }

        public long getTotalElements() { return totalElements; }
        public void setTotalElements(long totalElements) { this.totalElements = totalElements; }

        public int getTotalPages() { return totalPages; }
        public void setTotalPages(int totalPages) { this.totalPages = totalPages; }

        public int getNumber() { return number; }
        public void setNumber(int number) { this.number = number; }
    }
}