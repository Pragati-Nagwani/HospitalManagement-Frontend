package com.frontend.HospitalManagement.dto.TrainedIn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.frontend.HospitalManagement.dto.Procedure.ProcedureResponse;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainedInResponse {

    @JsonProperty("_embedded")
    private Embedded embedded;

    @JsonProperty("_links")
    private Map<String, Object> links;

    private ProcedureResponse.PageMetadata page;

    public Embedded getEmbedded() { return embedded; }
    public void setEmbedded(Embedded embedded) { this.embedded = embedded; }

    public ProcedureResponse.PageMetadata getPage() { return page; }
    public void setPage(ProcedureResponse.PageMetadata page) { this.page = page; }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Embedded {
        private List<TrainedInDto> trainedIns;

        public List<TrainedInDto> getTrainedIns() { return trainedIns; }
        public void setTrainedIns(List<TrainedInDto> trainedIns) { this.trainedIns = trainedIns; }
    }
}