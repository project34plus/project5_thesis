package org.choongang.thesis.services;

import org.choongang.global.ListData;
import org.choongang.thesis.controllers.ThesisSearch;
import org.choongang.thesis.entities.Thesis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ThesisInfoTest {

    @Autowired
    private ThesisInfoService infoService;

    private ThesisSearch search;

    @Test
    @DisplayName("논문 검색 테스트")
    void listSearchTest() {
        search = new ThesisSearch();
        search.setPoster("김정진");
//        search.setEDate(LocalDate.now());
        System.out.println(search);

        ListData<Thesis> result = infoService.getList(search);

        System.out.println(result);
    }
}
