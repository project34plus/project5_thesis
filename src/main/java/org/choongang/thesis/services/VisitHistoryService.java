package org.choongang.thesis.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.global.CommonSearch;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.member.MemberUtil;
import org.choongang.thesis.entities.QVisitHistory;
import org.choongang.thesis.entities.VisitHistory;
import org.choongang.thesis.repositories.VisitHistoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class VisitHistoryService {
    private final VisitHistoryRepository repository;
    private final MemberUtil memberUtil;
    private final HttpServletRequest request;

    public void saveList(Long tid) {
        if (!memberUtil.isLogin()) {
            return;
        }
        try {
            System.out.println("방문 기록 저장 시작"+tid);
            VisitHistory history = VisitHistory.builder()
                    .tid(tid)
                    .email(memberUtil.getMember().getEmail())
                    .viewdAt(LocalDateTime.now())
                    .build();

            VisitHistory savedHistory = repository.saveAndFlush(history);
            System.out.println("방문 기록 저장 완료");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("방문기록 조회 오류");
        }
    }

    public ListData<VisitHistory> getRecentVisits(CommonSearch search){
        if(!memberUtil.isLogin()){
            return new ListData<>();
        }

        int page = Math.max(search.getPage(),1);
        int limit = search.getLimit();
        limit = limit < 1 ? 5 : limit;

        QVisitHistory visitHistory = QVisitHistory.visitHistory;
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(visitHistory.email.eq(memberUtil.getMember().getEmail()));

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("viewdAt")));
        Page<VisitHistory> data = repository.findAll(builder,pageable);

        Pagination pagination = new Pagination(page,(int) data.getTotalElements(),10,limit,request);

        return new ListData<>(data.getContent(),pagination);

    }

}
