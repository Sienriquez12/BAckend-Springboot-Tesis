package com.especlub.match.clients;

import com.especlub.match.shared.exceptions.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationClient {

    private final RestTemplateBuilder restTemplateBuilder;

    @Value("${external.recommendations.base-url:https://ia-especlubsmatch-p87sg.ondigitalocean.app}")
    private String baseUrl;

    @Value("${external.recommendations.api-key:qR8s7V3kLp9WzX2mN5bU6yT1hJ4cF0aG_3dE2vY9}")
    private String apiKey;

    private RestTemplate restTemplate() {
        return restTemplateBuilder.build();
    }

    /**
     * Calls external recommendations endpoint and returns the parsed DTO.
     * Example URL: {baseUrl}/api/recommendations/{studentId}
     *
     * @param studentId student id to request recommendations for
     * @return ExternalRecommendationResponseDto parsed response
     */
    public com.especlub.match.clients.dto.ExternalRecommendationResponseDto getRecommendations(Long studentId) {
        if (studentId == null) throw new CustomExceptions("studentId es requerido", HttpStatus.BAD_REQUEST.value());

        String url = String.format("%s/api/recommendations/%d", baseUrl, studentId);
        try {
            log.debug("Requesting external recommendations: {}", url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("x-internal-ia", apiKey);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<com.especlub.match.clients.dto.ExternalRecommendationResponseDto> response =
                    restTemplate().exchange(url, HttpMethod.POST, entity, com.especlub.match.clients.dto.ExternalRecommendationResponseDto.class);

            com.especlub.match.clients.dto.ExternalRecommendationResponseDto resp = response.getBody();
            if (resp == null) throw new CustomExceptions("Respuesta vacía del servicio de recomendaciones", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return resp;
        } catch (RestClientException ex) {
            log.error("Error calling recommendations service: {}", ex.getMessage(), ex);
            throw new CustomExceptions("Error al obtener recomendaciones: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
