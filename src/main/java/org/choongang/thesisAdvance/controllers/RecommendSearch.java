package org.choongang.thesisAdvance.controllers;

import lombok.Data;
import org.choongang.thesis.controllers.ThesisSearch;

@Data
public class RecommendSearch extends ThesisSearch {
    private String fieldFilter;
}
