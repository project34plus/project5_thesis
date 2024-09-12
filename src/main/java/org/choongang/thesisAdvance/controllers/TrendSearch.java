package org.choongang.thesisAdvance.controllers;

import lombok.Data;
import org.choongang.thesis.controllers.ThesisSearch;

import java.time.LocalDate;

@Data
public class TrendSearch extends ThesisSearch {
    private String job; // 직업 정보
    private LocalDate sDate;
    private LocalDate eDate;
}
