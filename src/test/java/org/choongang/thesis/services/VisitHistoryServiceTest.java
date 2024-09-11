package org.choongang.thesis.services;

import org.choongang.global.CommonSearch;
import org.choongang.global.ListData;
import org.choongang.thesis.entities.VisitHistory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // 테스트가 끝나면 자동으로 롤백됩니다.
@Rollback // 필요시 false로 설정하여 데이터가 실제로 저장되도록 할 수 있습니다.
public class VisitHistoryServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VisitHistoryService visitHistoryService;

    @Test
    @DisplayName("방문 기록 저장 테스트")
    @Rollback(false)  // 데이터베이스에 저장된 내용을 유지하도록 설정
    @WithMockUser(username = "test01@test.org")
    void testSaveVisitHistory() throws Exception {
        Long tid = 610L;

        visitHistoryService.saveList(tid);

        // /info/{tid} 경로에 대한 GET 요청 실행
        mockMvc.perform(get("/info/{tid}", tid))
                .andDo(print())
                .andExpect(status().isOk());

        // 저장된 방문 기록을 DB에서 확인
        CommonSearch search = new CommonSearch();
        ListData<VisitHistory> visitHistoryList = visitHistoryService.getRecentVisits(search);

        // ListData가 null이 아닌지, 리스트가 null이 아닌지 확인
        assertNotNull(visitHistoryList, "방문 기록 리스트가 null입니다.");
        assertNotNull(visitHistoryList.getItems(), "방문 기록 리스트 항목이 null입니다.");
        assertFalse(visitHistoryList.getItems().isEmpty(), "방문 기록이 저장되지 않았습니다.");
    }

    @Test
    @DisplayName("최근 방문 기록 조회 테스트")
    @WithMockUser(username = "test01@test.org")
    void testGetRecentVisits() throws Exception {
        // /recent-visits 경로에 대한 GET 요청 실행
        mockMvc.perform(get("/recent-visits"))
                .andDo(print())
                .andExpect(status().isOk());

        // 최근 방문 기록을 DB에서 확인
        CommonSearch search = new CommonSearch();
        ListData<VisitHistory> visitHistoryList = visitHistoryService.getRecentVisits(search);

        assertNotNull(visitHistoryList, "방문 기록이 조회되지 않았습니다.");
        assertFalse(visitHistoryList.getItems().isEmpty(), "조회된 방문 기록이 없습니다.");
    }
}
