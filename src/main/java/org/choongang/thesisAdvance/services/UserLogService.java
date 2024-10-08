package org.choongang.thesisAdvance.services;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.entities.UserLog;
import org.choongang.thesis.entities.VisitHistory;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.ThesisRepository;
import org.choongang.thesis.repositories.UserLogRepository;
import org.choongang.thesis.repositories.VisitHistoryRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserLogService {
    private final MemberUtil memberUtil;
    private final UserLogRepository userLogRepository;
    private final ThesisRepository thesisRepository;
    private final VisitHistoryRepository visitHistoryRepository;
    private JPAQueryFactory queryFactory;

    /**
     * @Id @GeneratedValue
     * private Long logSeq;
     * @ManyToOne(fetch = FetchType.LAZY)
     * @JoinColumn(name="tid") private Thesis thesis;
     * @CreatedBy
     * @Column(length=80, nullable = false)
     * private String email;
     * @Column(length=80, nullable = false)
     * private String search; // 검색어
     * @CreatedDate private LocalDateTime searchDate; // 검색일
     */
    /**
     * 검색 검색어 기록
     *
     * @param keyword
     */
    public void save(String keyword) {
        if (!memberUtil.isLogin() || !StringUtils.hasText(keyword.trim())) {
            return;
        }
        try {
            Member member = memberUtil.getMember();
            UserLog userLog = UserLog.builder()
//                    .email(memberUtil.getMember().getEmail())
                    .email(SecurityContextHolder.getContext().getAuthentication().getName())
                    .job(member.getJob())
                    .search(keyword)
                    .build();
            userLogRepository.saveAndFlush(userLog);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("검색 기록 실패");
        }
    }

    /**
     * 논문 조회 기록
     *
     * @param tid
     */
    public void save(Long tid) {
//        if (!memberUtil.isLogin() || tid == null){
//            System.out.println("user 정보 또는 게시글 번호 없음");
//            return;
//        }
        try {
            VisitHistory visitHistory = VisitHistory.builder()
//                    .email(memberUtil.getMember().getEmail())
                    .email(SecurityContextHolder.getContext().getAuthentication().getName())
                    .thesis(thesisRepository.findById(tid).orElseThrow(ThesisNotFoundException::new))
                    .build();
            visitHistoryRepository.saveAndFlush(visitHistory);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("논문 조회 기록 실패");
        }
    }

    public List<Thesis> getRecentlyViewedTheses(String email) {
        List<VisitHistory> logs = visitHistoryRepository.findByEmailOrderBySearchDateDesc(email);
        List<Long> tids = logs.stream().map(log -> log.getThesis().getTid()).collect(Collectors.toList());
        return thesisRepository.findAllById(tids);

    }

}
