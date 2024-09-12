package org.choongang.global;

import lombok.Data;

import java.util.List;

@Data
public class AdvancedSearch extends CommonSearch { //연산자 추가
    private List<String> sopts;
    private List<String> skeys;
    private List<String> operators;
}
