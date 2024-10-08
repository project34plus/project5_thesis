package org.choongang.thesis.controllers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.choongang.file.entities.FileInfo;
import org.choongang.thesis.constants.ApprovalStatus;

import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestThesis {

    private Long tid;


    private String mode; //register, update

    private String actionType; //등록, 재등록 구분

    @NotBlank
    private String category;


    private List<String> fields; //학문별 분류 코드 목록

    @NotBlank
    private String poster; // 편집자

    @NotBlank
    private String title; // 제목

    private String contributor; // 기여자

    private String thAbstract; // 초록

    private String keywords; // 키워드

    private String reference; // 참고 문헌


    private String gid = UUID.randomUUID().toString(); // 그룹 ID

    private boolean visible; //  공개 여부

    private  boolean approval; //승인여부

    @NotBlank
    private String publisher; // 발행기관

    private String toc; // 목차

    private String language; // 언어

    private String country; // 국가

    private FileInfo fileInfo; //논문 파일

    private int majorVersion; // 버전관리

    private int minorVersion; // 버전관리

    private String rejectedReason;

    private String userName;

    private String email;


    private ApprovalStatus approvalStatus; //승인 상태 필드

}
