package org.choongang.thesis.services;

import org.choongang.thesis.constants.Category;
import org.choongang.thesis.controllers.ThesisApprovalRequest;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.repositories.ThesisRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
@Commit
public class ThesisApprovalTest {

    @Autowired
    private ThesisRepository thesisRepository;

    @Autowired
    private ThesisSaveService thesisSaveService;

    @Autowired
    private ThesisDeleteService thesisDeleteService;


    @Test
    public void testThesisApprovalUpdate() {
        // 1. 논문 10개 저장
        for (int i = 1; i <= 10; i++) {
            Thesis thesis = Thesis.builder()
                    .title("Test Thesis " + i)
                    .poster("Poster " + i)
                    .category(Category.DOMESTIC)
                    .gid("GID" + i)
                    .visible(true)
                    .approval(false)
                    .userName("Test User")
                    .email("testuser" + i + "@email.com")
                    .build();

            thesisRepository.save(thesis);
        }
        thesisRepository.flush();
        // 2. 논문 번호 8번과 9번만 승인 상태를 true로 설정
        List<ThesisApprovalRequest.ThesisApprovalItem> thesisApprovalItems = thesisRepository.findAll().stream()
                .filter(thesis -> thesis.getTid() == 8 || thesis.getTid() == 9) // 8번, 9번 논문만 선택
                .map(thesis -> {
                    ThesisApprovalRequest.ThesisApprovalItem item = new ThesisApprovalRequest.ThesisApprovalItem();
                    item.setThesisId(thesis.getTid());
                    item.setApproved(true); // 8번, 9번 논문 승인 처리
                    return item;
                })
                .collect(Collectors.toList());

        ThesisApprovalRequest approvalRequest = new ThesisApprovalRequest();
        approvalRequest.setTheses(thesisApprovalItems);


        thesisSaveService.saveTheses(approvalRequest.getTheses());


        List<Thesis> updatedTheses = thesisRepository.findAll();

        List<Long> thesesToDelete = updatedTheses.subList(0, 4).stream()
                .map(Thesis::getTid)
                .collect(Collectors.toList());

        thesisDeleteService.deleteList(thesesToDelete); // 4개 논문 삭제

        // 6. 삭제 후 논문 개수 확인 (6개 남아야 함)
        List<Thesis> remainingTheses = thesisRepository.findAll();
        assertEquals(6, remainingTheses.size()); // 6개 남아야 함
    }
}
