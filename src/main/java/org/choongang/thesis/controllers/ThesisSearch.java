package org.choongang.thesis.controllers;

import lombok.Data;
import org.choongang.global.CommonSearch;
import org.choongang.thesis.constants.ApprovalStatus;

import java.util.List;

@Data
public class ThesisSearch extends CommonSearch {
    private List<String> category;
    private List<String> fields;
    private ApprovalStatus approvalStatus; //승인 상태 필드
    private List<String> email; //회원 이메일(로그인 ID)

}
