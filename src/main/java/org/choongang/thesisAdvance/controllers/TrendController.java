package org.choongang.thesisAdvance.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.choongang.global.rests.JSONData;
import org.choongang.member.MemberUtil;
import org.choongang.thesisAdvance.services.TrendInfoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trend")
public class TrendController {

    private final TrendInfoService trendInfoService;
    private final MemberUtil memberUtil;

    @Operation(summary = "직업 그룹별 인기 키워드 조회", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping("/keyword")
    @PreAuthorize("permitAll()")
    public JSONData getKeywordByJob(@ModelAttribute TrendSearch search){
        List<Map<String, Object>> items = trendInfoService.getKeywordRankingByJob(search);

        return new JSONData(items);
    }

    @Operation(summary = "학문 분류별 트렌드 통계", method = "GET")
    @ApiResponse(responseCode = "200")
    @GetMapping("/field")
    @PreAuthorize("permitAll()")
    //@PreAuthorize("hasAnyAuthority('ADMIN')") //테스트용
    public JSONData getFieldRanking(@ModelAttribute TrendSearch search){
        Map<String, Map<String, Object>> data = trendInfoService.getFieldRanking(search);

        //System.out.printf("isLogin: %s, isAdmin: %s, member: %s%n", memberUtil.isLogin(), memberUtil.isAdmin(), memberUtil.getMember());

        return new JSONData(data);
    }
}