package org.choongang.thesis.services.comment;

import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.thesis.controllers.RequestComment;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.services.ThesisInfoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentTest {

    @Autowired
    private MemberUtil memberUtil;

    @Autowired
    private ThesisInfoService infoService;

    @Autowired
    private CommentSaveService commentSaveService;

    @Test
    @DisplayName("댓글 작성 테스트")
    void commentSaveTest() {

        Member member = memberUtil.getMember();
        //시큐리티 걸림, 토큰 발급 참고해야 할 듯, 어떻게 하죠?

        Long tid = 1L;
        Thesis thesis = infoService.get(tid); //논문 상세 조회

        System.out.println(thesis);

        RequestComment form = new RequestComment();

        form.setTid(thesis.getTid());
        form.setUsername(member.getUserName());
        form.setContent("good");

        commentSaveService.save(form);
    }
}
