package org.choongang.thesis.validators;

import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.choongang.thesis.controllers.RequestComment;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class CommentValidator implements Validator {

    private final MemberUtil memberUtil;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RequestComment.class);
    }

    @Override
    public void validate(Object target, Errors errors) {

        if (memberUtil.isLogin()) { // 로그인 상태 일때는 비회원 비밀번호 체크 X
            return;
        }
    }
}
