package org.choongang.thesisAdvance.services;


import lombok.RequiredArgsConstructor;
import org.choongang.global.Utils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final RestTemplate restTemplate;
    private final Utils utils; // URL 생성 도구

    public List<String> getEmailsByJob(String job) {
        // 1. API 호출을 위한 URL 구성
        String url = utils.url("/api/members", "member-service") + "?job=" + job;

        // 2. 헤더 및 요청 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        // 3. API 호출
        ResponseEntity<List<String>> response = restTemplate.exchange(
                URI.create(url),
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {});

        // 4. 응답으로부터 이메일 리스트 반환
        return response.getBody();
    }
}
