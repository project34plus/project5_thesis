package org.choongang.thesis.services;

import org.choongang.global.ListData;
import org.choongang.thesis.controllers.ThesisSearch;
import org.choongang.thesis.entities.Thesis;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class ListSearchTest {

    @Autowired
    private ThesisInfoService infoService;

    private ThesisSearch search;

    @Test
    @DisplayName("논문 통합 검색 테스트")
    void listSearchTest() {
        search = new ThesisSearch();
        search.setPublisher("학회");
        search.setEDate(LocalDate.now());
//        search.setPoster("임윤진");

        System.out.println(search);

        ListData<Thesis> result = infoService.getList(search);

        System.out.println(result);
    }

    @Test
    @DisplayName("고급 검색 테스트")
    void advancedSearchTest() {
        search = new ThesisSearch();

        //어떻게 하는 거지..
        search.setSopts(List.of("publisher", "title"));
        search.setOperators(List.of("AND"));
        search.setSkeys(List.of("학회", "고등"));
        /*
            http://localhost:4003/list?
            sopts=publisher&skeys=학회&
            operators=AND&
            sopts=title&skeys=고등
        */

        System.out.println(search);

        System.out.println("sopts: " + search.getSopts());
        System.out.println("skeys: " + search.getSkeys());
        System.out.println("opers: " + search.getOperators());

        ListData<Thesis> result = infoService.getList(search);
        System.out.println("result: "+ result);
    }
}
