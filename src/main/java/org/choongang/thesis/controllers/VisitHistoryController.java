package org.choongang.thesis.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.global.CommonSearch;
import org.choongang.global.ListData;
import org.choongang.thesis.entities.VisitHistory;
import org.choongang.thesis.services.VisitHistoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recent-visits")
@RequiredArgsConstructor
public class VisitHistoryController {

    private final VisitHistoryService visitHistoryService;

    @GetMapping
    @PreAuthorize("isAuthenticated()") // 로그인한 사용자만 접근 가능
    public ListData<VisitHistory> getRecentVisits(CommonSearch search) {
        return visitHistoryService.getRecentVisits(search);
    }
}
