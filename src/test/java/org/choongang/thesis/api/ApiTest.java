package org.choongang.thesis.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.choongang.thesis.constants.Category;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.repositories.ThesisRepository;
import org.json.JSONObject;
import org.json.XML;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;
import java.util.Map;

@SpringBootTest
//@ActiveProfiles("dev")
public class ApiTest {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private ObjectMapper om;

    // XML을 JSON으로 변환하는 메서드
    public static String convertXmlToJson(String xml) {
        JSONObject jsonObject = XML.toJSONObject(xml);
        return jsonObject.toString(4); // 들여쓰기 포함
    }

    @Test
    void test1() throws Exception {

        String url = "https://open.kci.go.kr/po/openapi/openApiSearch.kci?apiCode=articleDetail&key=13281654&id=ART002358582";

        ResponseEntity<String> response = restTemplate.getForEntity(URI.create(url), String.class); //URL에 HTTP GET 요청을 보내고 응답 받아옴

        // xml을 json으로 변환
        String jsonResponse = convertXmlToJson(response.getBody());

        Map<String, Object> responseMap = om.readValue(jsonResponse, Map.class);

        // MetaData -> outputData -> record -> articleInfo에 접근
        Map<String, Object> metaData = (Map<String, Object>) responseMap.get("MetaData");
        Map<String, Object> outputData = (Map<String, Object>) metaData.get("outputData");
        Map<String, Object> record = (Map<String, Object>) outputData.get("record");

        // articleInfo 추출
        Map<String, Object> articleInfo = (Map<String, Object>) record.get("articleInfo");

        // 논문 제목 (title-group의 첫 번째 제목 사용)
        // title-group을 처리 (LinkedHashMap 형태)
        Map<String, Object> titleGroup = (Map<String, Object>) articleInfo.get("title-group");
        List<Map<String, Object>> articleTitles = (List<Map<String, Object>>) titleGroup.get("article-title");

        // 첫 번째 제목을 추출 (original, foreign, english 중 첫 번째 original 선택)
        String title = null;
        if (articleTitles != null && !articleTitles.isEmpty()) {
            title = (String) articleTitles.get(0).get("content");
        }


        // 작성자
        String poster = (String) ((Map<String, Object>) ((Map<String, Object>) articleInfo.get("author-group")).get("author")).get("name");

        // 초록 (abstract 내용 추출)
        String thAbstract = (String) ((Map<String, Object>) articleInfo.get("abstract")).get("content");

        // 발행기관 (journalInfo에서 publisher-name 추출)
        String publisher = (String) ((Map<String, Object>) record.get("journalInfo")).get("publisher-name");

        // 참조 데이터 (citation-count 추출, 예: kci 값을 참조로 사용)
        String reference = (String) ((Map<String, Object>) articleInfo.get("citation-count")).get("kci").toString();

        // 언어 추출
        String language = (String) articleInfo.get("article-language");

        // 기타 고정된 값 예시
        String country = "한국";
        Category category = Category.DOMESTIC;
        String userName = "관리자";  // 이 값을 동적으로 바꾸려면 로그인 정보를 참조
        String email = "admin@example.com";  // 이 값도 동적으로 바꾸려면 로그인 정보를 참조
        boolean approval = true;  // 승인 여부는 일단 고정 값으로 설정

        // 논문 엔티티 생성
        Thesis thesis = Thesis.builder()
                .title(title)
                .category(category)
                .poster(poster)
                .thAbstract(thAbstract)
                .publisher(publisher)
                .reference(reference)
                .language(language)
                .country(country)
                .userName(userName)
                .email(email)
                .approval(approval)
                .gid("ART002358582")  // 논문의 article-id를 그룹 ID로 사용
                .build();

        // 변환된 Thesis 객체를 데이터베이스에 저장
        thesisRepository.saveAndFlush(thesis);

        // 저장된 Thesis 확인을 위해 출력
        System.out.println("저장된 Thesis: " + thesis);


    }
}
