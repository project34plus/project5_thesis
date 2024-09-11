package org.choongang.thesisAdvance.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileInfoService;
import org.choongang.global.Pagination;
import org.choongang.member.MemberUtil;
import org.choongang.thesis.controllers.ThesisSearch;
import org.choongang.thesis.entities.QThesis;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.repositories.ThesisRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
public class TrendInfoService {
    private final ThesisRepository thesisRepository;
    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;
    private final MemberUtil memberUtil;

    public List<Thesis> getListByDateRange(ThesisSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();

        /* 검색 처리 S */
        QThesis thesis = QThesis.thesis;
        BooleanBuilder andBuilder = new BooleanBuilder();

        String sopt = search.getSopt();
        String skey = search.getSkey();
        List<String> category = search.getCategory();
        List<String> fields = search.getFields();
        List<String> email = search.getEmail();

        if (search.getApproval() != null) {
            boolean approval = memberUtil.isAdmin() ? search.getApproval() : true;
            andBuilder.and(thesis.approval.eq(search.getApproval()));
        }

        //작성한 회원 이메일로 조회
        if(email != null && !email.isEmpty()) {
            andBuilder.and(thesis.email.in(email));
        }

        if (!memberUtil.isAdmin()) {
            andBuilder.and(thesis.visible.eq(true))
                    .and(thesis.approval.eq(true));

        }

        // 기간 검색
        if(sDate != null) {
            andBuilder.and(thesis.createdAt.after(sDate.atStartOfDay()));
        }
        if(eDate != null) {
            andBuilder.and(thesis.createdAt.before(eDate.atStartOfDay()));
        }

        /* 검색 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Thesis> data = thesisRepository.findAll(andBuilder, pageable);

        long total = data.getTotalElements();
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        List<Thesis> items = data.getContent();

        return null;
                //new ListData<>(items, pagination);
    }
}
