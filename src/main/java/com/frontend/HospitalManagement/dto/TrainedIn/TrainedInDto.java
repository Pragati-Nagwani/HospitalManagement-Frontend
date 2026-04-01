package com.frontend.HospitalManagement.dto.TrainedIn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TrainedInDto {

    private PhysicianInfo physicianEntity;
    private Date certificationDate;
    private Date certificationExpires;
    private boolean hasExpired;
    private Integer physician;
    private Integer treatment;

    // No more _links parsing needed at top level — ID comes from physicianEntity._links.self
    @JsonProperty("_links")
    public void setLinks(Map<String, Object> links) {
        // Extract treatment from trainedIn self link: .../trainedIn/2
        if (links != null) {
            Object selfObj = links.get("self");
            if (selfObj instanceof Map) {
                String href = (String) ((Map<?, ?>) selfObj).get("href");
                if (href != null) {
                    try {
                        String idStr = href.substring(href.lastIndexOf("/") + 1);
                        this.treatment = Integer.parseInt(idStr);
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PhysicianInfo {
        private String name;
        private Integer physicianId; // We'll populate this from _links.self

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getPhysicianId() { return physicianId; }

        @JsonProperty("_links")
        public void setLinks(Map<String, Object> links) {
            if (links != null) {
                Object selfObj = links.get("self");
                if (selfObj instanceof Map) {
                    String href = (String) ((Map<?, ?>) selfObj).get("href");
                    if (href != null) {
                        try {
                            // href = "http://localhost:8080/allPhysician/1"
                            String idStr = href.substring(href.lastIndexOf("/") + 1);
                            if (idStr.contains("{")) idStr = idStr.substring(0, idStr.indexOf("{"));
                            this.physicianId = Integer.parseInt(idStr.trim());
                            System.out.println("DEBUG: Extracted physician ID = " + this.physicianId);
                        } catch (Exception e) {
                            System.err.println("DEBUG: Failed to parse physician ID from: " + href);
                        }
                    }
                }
            }
        }
    }

    // Convenience getter so Thymeleaf can still use t.physician
    public Integer getPhysician() {
        if (physicianEntity != null) return physicianEntity.getPhysicianId();
        return physician;
    }
    public void setPhysician(Integer physician) { this.physician = physician; }

    public PhysicianInfo getPhysicianEntity() { return physicianEntity; }
    public void setPhysicianEntity(PhysicianInfo physicianEntity) { this.physicianEntity = physicianEntity; }

    public Date getCertificationDate() { return certificationDate; }
    public void setCertificationDate(Date certificationDate) { this.certificationDate = certificationDate; }

    public Date getCertificationExpires() { return certificationExpires; }
    public void setCertificationExpires(Date certificationExpires) { this.certificationExpires = certificationExpires; }

    public boolean isHasExpired() { return hasExpired; }
    public void setHasExpired(boolean hasExpired) { this.hasExpired = hasExpired; }

    public Integer getTreatment() { return treatment; }
    public void setTreatment(Integer treatment) { this.treatment = treatment; }
}
