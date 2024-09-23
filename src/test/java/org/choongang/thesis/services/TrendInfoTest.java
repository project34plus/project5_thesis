package org.choongang.thesis.services;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.global.ListData;
import org.choongang.thesis.controllers.ThesisSearch;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.choongang.thesisAdvance.controllers.TrendSearch;
import org.choongang.thesisAdvance.services.TrendInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootTest
public class TrendInfoTest {

    @Autowired
    ThesisInfoService infoService;

    @Autowired
    ThesisRepository thesisRepository;

    @Autowired
    FieldRepository fieldRepository;

    @Autowired
    TrendInfoService trendInfoService;

    private HttpServletRequest request;

    /*
    @BeforeEach
    void Data() {
        for(int i = 0; i < 10; i++){
            Thesis thesis = new Thesis();
            thesis.setCategory(Category.DOMESTIC);
            thesis.setPoster("편집자"+i);
            thesis.setTitle("제목"+i);
            thesis.setGid("그룹 ID"+i);
            thesis.setUserName("userName"+i);
            thesis.setEmail("test"+i+"@email.org");

            thesisRepository.saveAndFlush(thesis);
        }
    }*/


    @Test
    @DisplayName("기간별 논문 조회 테스트")
    void test1() {
        LocalDate eDate = LocalDate.now().plusDays(1L);
        ThesisSearch search = new ThesisSearch();
        search.setEDate(eDate);
        ListData<Thesis> theses = infoService.getList(search);
        //theses.getItems().forEach(System.out::println);
        System.out.println("Theses count: " + theses.getItems().size());
        //theses.getItems().forEach(i ->System.out.println("Thesis: " + i));
    }

    @Test
    @DisplayName("필드별 조회수, 찜하기 수 조회 테스트")
    void testFieldRanking() {

        LocalDate sDate = LocalDate.of(2024, 8, 20);
        LocalDate eDate = LocalDate.of(2024, 9, 20);

        TrendSearch search = new TrendSearch();
        search.setSDate(sDate);
        search.setEDate(eDate);

        String mainCategory = "공학"; // 예시로 "공학" 대분류 선택
        Map<String, Map<String, Object>> result = trendInfoService.getFieldRanking(search);

        Map<String, Map<String, Object>> filteredResult = result.entrySet().stream()
                .filter(entry -> mainCategory.equals(entry.getValue().get("name"))) // 대분류 기준으로 필터링
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        System.out.println("선택한 학문의 중분류: " + filteredResult); // 중분류별 count, wishCount 출력

        System.out.println("찜하기 수: " + filteredResult.get("wishCount")); // null

        /*
        QField field = QField.field;
        List<Field> fields = (List<Field>) fieldRepository.findAll(field.id.in("S"));
        Map<String, Map<String, Object>> result = trendInfoService.getFieldRanking(search);

        //System.out.println(result);
        System.out.println(result.get(fields).get("wishCount"));
         */
    }
}
