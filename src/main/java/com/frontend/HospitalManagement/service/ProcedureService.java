package com.frontend.HospitalManagement.service;



import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.frontend.HospitalManagement.config.WebClientConfig;
import com.frontend.HospitalManagement.dto.Procedure.ProcedureDto;
import com.frontend.HospitalManagement.dto.Procedure.ProcedureResponse;
import com.frontend.HospitalManagement.dto.TrainedIn.TrainedInResponse;

@Service
public class ProcedureService {

    private final WebClient webClient;

public ProcedureService(WebClient webClient) {
    this.webClient = webClient;
}

public ProcedureResponse getProcedures(int page, int size) {
    return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/procedures")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build())
            .retrieve()
            .bodyToMono(ProcedureResponse.class)
            .block();
}

public ProcedureResponse searchByName(String name, int page, int size) {
    return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/procedures/search/findByNameContainingIgnoreCase")
                    .queryParam("name", name)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build())
            .retrieve()
            .bodyToMono(ProcedureResponse.class)
            .block();
}

public String addProcedure(ProcedureDto procedure) {
    // Check if ID already exists
    boolean exists = false;
    try {
        webClient.get()
                .uri("/procedures/" + procedure.getCode())
                .retrieve()
                .toBodilessEntity()
                .block();
        exists = true;
    } catch (Exception e) {
        exists = false;
    }

    if (exists) {
        return "id already exists";
    }

    try {
        webClient.post()
                .uri("/procedures")
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .bodyValue(procedure)
                .retrieve()
                .toBodilessEntity()
                .block();
        return "SUCCESS";
    } catch (Exception e) {
        e.printStackTrace();
        return "Failed to add procedure";
    }
}
public void updateProcedureCost(Integer code, Double cost) {
    String body = "{\"cost\": " + cost + "}";
    webClient.patch()
            .uri("/procedures/" + code)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
}

public TrainedInResponse getTrainedInByProcedure(Integer treatment, int page, int size) {
    
    // DEBUG: print raw JSON first
    String raw = webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/trainedIn/search/findByTreatment")
                    .queryParam("treatment", treatment)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .queryParam("projection", "viewCertified")
                    .build())
            .retrieve()
            .bodyToMono(String.class)
            .block();

    System.out.println("====== RAW TRAINED-IN JSON ======");
    System.out.println(raw);
    System.out.println("=================================");

    // Now fetch again as the actual DTO
    return webClient.get()
            .uri(uriBuilder -> uriBuilder
                    .path("/trainedIn/search/findByTreatment")
                    .queryParam("treatment", treatment)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .queryParam("projection", "viewCertified")
                    .build())
            .retrieve()
            .bodyToMono(TrainedInResponse.class)
            .block();
}

public void renewTrainedIn(Integer tId, Integer pId, String certDate, String expDate) {
    // This Map matches the JSON keys your backend expects
    Map<String, Object> body = Map.of(
        "certificationDate", certDate,
        "certificationExpires", expDate
    );

    webClient.patch()
            .uri("/trainedIn/renew/{tId}/{pId}", tId, pId)
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(Void.class)
            .block(); // block() is okay here since we want to wait for the update before redirecting
}

public void addTrainedIn(Integer treatment, Integer physician, String certDate, String expDate) {
    Map<String, Object> body = Map.of(
        "physician",           physician,
        "treatment",           treatment,
        "certificationDate",   certDate,
        "certificationExpires", expDate
    );

    webClient.post()
            .uri("/trainedIn")
            .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
            .bodyValue(body)
            .retrieve()
            .onStatus(
                status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .map(b -> new RuntimeException(b))
            )
            .bodyToMono(Void.class)
            .block();
}

}