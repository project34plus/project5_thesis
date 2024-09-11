package org.choongang.thesis.services;

import org.choongang.thesis.entities.Interests;
import org.choongang.thesis.exceptions.InterestNotFoundException;
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
        String email = "test02@test.org";
        List<Interests> list = List.of(
                new Interests(fieldRepository.findById("SS-Law").orElseThrow(InterestNotFoundException::new).getId(), email),
                new Interests(fieldRepository.findById("AE-Ecology").orElseThrow(InterestNotFoundException::new).getId(), email),
                new Interests(fieldRepository.findById("BE-Marketing").orElseThrow(InterestNotFoundException::new).getId(), email)
        );
        interestSaveService.save(list, email);
        System.out.println(list);
    }
    @Test
    @DisplayName("관심사 수정 테스트")
    void test2() throws Exception {
        List<Interests> list = List.of(new Interests(fieldRepository.findById("AE-AgriculturalScience").orElseThrow(InterestNotFoundException::new).getId(), "test01@test.org"));
        interestSaveService.save(list, "test01@test.org");
        System.out.println(list);
    }
}
