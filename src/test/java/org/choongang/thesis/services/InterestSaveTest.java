package org.choongang.thesis.services;

import org.choongang.thesis.entities.Interests;
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
        List<Interests> interests = List.of(new Interests("N-002",email));

        interestSaveService.save(interests, email);
        System.out.println(interests);
    }
    @Test
    @DisplayName("관심사 수정 테스트")
    void test2() throws Exception {
        String email = "test01@test.org";
        List<Interests> interests = List.of(new Interests("N-005",email));
        interestSaveService.save(interests, email);
        System.out.println(interests);
    }
}
