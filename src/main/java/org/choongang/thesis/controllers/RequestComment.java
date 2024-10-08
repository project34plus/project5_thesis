package org.choongang.thesis.controllers;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RequestComment {
    private String mode = "write";

    private Long seq; // 댓글 등록번호

    private Long tid; // 논문 번호

    @NotBlank
    private String userName; // 작성자=회원명

    @NotBlank
    private String content; // 댓글 내용
}
