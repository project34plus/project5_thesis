package org.choongang.thesis.services;

import org.choongang.thesis.repositories.FieldRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class InterestSaveTest {
    @Autowired
    private InterestSaveService interestSaveService;
    @Autowired
    private FieldRepository fieldRepository;

    @Test
    @DisplayName("관심사 저장 테스트")
    void test1() throws Exception {
        String email = "test01@test.org";
        List<String> interests = List.of("인문학(문학)","인문학(중국어와문학)","사회과학(법학)");

        interestSaveService.save(interests, email);
        System.out.println(interests);
    }
    @Test
    @DisplayName("관심사 수정 테스트")
    void test2() throws Exception {
        List<String> list = List.of("인문학(사전학)");
        interestSaveService.save(list, "test02@test.org");
        System.out.println(list);
    }
}
