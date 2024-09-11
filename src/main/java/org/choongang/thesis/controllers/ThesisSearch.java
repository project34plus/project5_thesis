package org.choongang.thesis.controllers;

import lombok.Data;
import org.choongang.global.CommonSearch;

import java.time.LocalDate;
import java.util.List;

@Data
public class ThesisSearch extends CommonSearch {
    private List<String> category;
    private List<String> fields;
    private Boolean approval;
    private List<String> email; //회원 이메일(로그인 ID)

    private LocalDate sDate;
    private LocalDate eDate;

    private String period;
}
