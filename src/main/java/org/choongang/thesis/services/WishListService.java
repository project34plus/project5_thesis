package org.choongang.thesis.services;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.choongang.global.Utils;
import org.choongang.member.MemberUtil;
import org.choongang.thesis.entities.QWishList;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.entities.WishList;
import org.choongang.thesis.entities.WishListId;
import org.choongang.thesis.repositories.ThesisRepository;
import org.choongang.thesis.repositories.WishListRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishListService {

    private final MemberUtil memberUtil;
    private final WishListRepository wishListRepository;
    private final ThesisRepository thesisRepository;
    private final Utils utils;

    @PreAuthorize("isAuthenticated()")
    public void add(Long tid) {
        WishList wishList = WishList.builder()
                .tid(tid)
                .build();

        wishListRepository.saveAndFlush(wishList);
    }

//    @PreAuthorize("isAuthenticated()")
    public void remove(Long tid) {
        WishListId wishListId = new WishListId(tid, memberUtil.getMember().getEmail());
        wishListRepository.deleteById(wishListId);
        wishListRepository.flush();
        System.out.println("위시리스트 논문 삭제");
    }

    @PreAuthorize("isAuthenticated()")
    public List<Long> getList() {
        if (!memberUtil.isLogin()) {
            return Collections.EMPTY_LIST;
        }
        // 1. 로그인된 사용자의 위시리스트 항목을 가져옴
        BooleanBuilder builder = new BooleanBuilder();
        QWishList wishList = QWishList.wishList;

        // 2. 현재 사용자의 이메일로 위시리스트 항목 필터링
        builder.and(wishList.email.eq(memberUtil.getMember().getEmail()));

        // 3. 위시리스트에서 논문 ID 리스트 추출
        List<Long> thesisIds = ((List<WishList>) wishListRepository.findAll(builder, Sort.by(Sort.Order.desc("createdAt"))))
                .stream()
                .map(WishList::getTid)
                .toList();

        // 4. 추출한 논문 ID 중에서 논문이 삭제되지 않은 (deletedAt이 null인) 논문만 필터링
        List<Long> validThesisIds = thesisRepository.findByTidInAndDeletedAtIsNull(thesisIds)
                .stream()
                .map(Thesis::getTid)
                .toList();

        return validThesisIds;
    }

    public long getCount(Long tid) {
        QWishList wishList = QWishList.wishList;

        return wishListRepository.count(wishList.tid.eq(tid));
    }

}
