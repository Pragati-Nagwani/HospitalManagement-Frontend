package com.frontend.HospitalManagement.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.frontend.HospitalManagement.dto.Nurse.NurseDTO;
import com.frontend.HospitalManagement.dto.Nurse.NursePageResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class NurseApiService {

    @Autowired
    private WebClient webClient;

    public NurseApiService(WebClient webClient) {
        this.webClient = webClient;
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 🔥 COMMON METHOD (reuse like your RoomService)
    public String call(String url, HttpMethod method, Object body) {

        WebClient.RequestBodySpec request = webClient
                .method(method)
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON);

        if (body != null) {
            return request.bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        }

        return request.retrieve()
                .bodyToMono(String.class)
                .block();
    }

    // ===========================
    // ✅ GET NURSES
    // ===========================
    public NursePageResponse getNurses(int page, int size, String keyword, String positionFilter) {

        String json = call("/nurse?projection=nurseView&page=0&size=1000", HttpMethod.GET, null);

        List<NurseDTO> allNurses = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(json);
            JsonNode nurses = root.path("_embedded").path("nurses");

            for (JsonNode node : nurses) {

                NurseDTO dto = new NurseDTO();

                // ✅ Extract ID from link
                String href = node.path("_links").path("self").path("href").asText();
                Integer id = Integer.parseInt(href.substring(href.lastIndexOf("/") + 1));
                dto.setEmployeeId(id);

                dto.setName(node.path("name").asText());
                dto.setPosition(node.path("position").asText());
                dto.setRegistered(node.path("registered").asBoolean());
                dto.setAvailability(node.path("availability").asText());

                allNurses.add(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error parsing nurse data", e);
        }

        // 🔍 SEARCH
        if (keyword != null && !keyword.isEmpty()) {
            allNurses = allNurses.stream()
                    .filter(n -> n.getName().toLowerCase().contains(keyword.toLowerCase())
                            || n.getPosition().toLowerCase().contains(keyword.toLowerCase()))
                    .toList();
        }

        // 🎯 FILTER
        if (positionFilter != null && !positionFilter.isEmpty()) {
            allNurses = allNurses.stream()
                    .filter(n -> n.getPosition().equalsIgnoreCase(positionFilter))
                    .toList();
        }

        // 📄 PAGINATION
        int start = page * size;
        int end = Math.min(start + size, allNurses.size());

        List<NurseDTO> pageList = new ArrayList<>();
        if (start < allNurses.size()) {
            pageList = allNurses.subList(start, end);
        }

        int totalPages = (int) Math.ceil((double) allNurses.size() / size);

        NursePageResponse result = new NursePageResponse();
        result.setNurses(pageList);
        result.setTotalPages(totalPages);

        return result;
    }

    public void addNurse(NurseDTO nurse) {

        Map<String, Object> request = new HashMap<>();

        request.put("employeeId", nurse.getEmployeeId());
        request.put("name", nurse.getName());
        request.put("position", nurse.getPosition());
        request.put("registered", nurse.isRegistered());
        request.put("ssn", nurse.getSsn()); // ✅ USER INPUT

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

        webClient.post()
                .uri("/nurse")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void updateNurse(Integer id, NurseDTO nurse) {

        Map<String, Object> request = new HashMap<>();

        request.put("name", nurse.getName());
        request.put("position", nurse.getPosition());
        request.put("registered", nurse.isRegistered());
        request.put("ssn", nurse.getSsn());

        webClient.put()
                .uri("/nurse/" + id)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
    public NurseDTO getNurseById(Integer id) {

        Map<String, Object> response = webClient.get()
                .uri("/nurse/" + id + "?projection=nurseView")
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        NurseDTO dto = new NurseDTO();

        dto.setEmployeeId(id);
        dto.setName((String) response.get("name"));
        dto.setPosition((String) response.get("position"));
        dto.setRegistered((Boolean) response.get("registered"));

        return dto;
    }
    public Map<String, Object> getAppointmentByNurse(Integer nurseId) {

        String uri = "/appointments/search/byNurse?nurse=http://localhost:9090/nurse/" + nurseId + "&projection=appointmentView";

        Map<String, Object> response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> embedded = (Map<String, Object>) response.get("_embedded");

        if (embedded == null || embedded.get("appointments") == null) return null;

        List<Map<String, Object>> list =
                (List<Map<String, Object>>) embedded.get("appointments");

        if (list.isEmpty()) return null;

        return list.get(0); // first appointment
    }

    public Map<String, Object> getOnCallByNurse(Integer nurseId) {

        String uri = "/oncalls/search/byNurse?nurse=" + nurseId;

        Map<String, Object> response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        Map<String, Object> embedded = (Map<String, Object>) response.get("_embedded");

        if (embedded == null || embedded.get("oncalls") == null) return null;

        List<Map<String, Object>> list =
                (List<Map<String, Object>>) embedded.get("oncalls");

        if (list.isEmpty()) return null;

        return list.get(0);
    }


}