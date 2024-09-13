package org.choongang.thesis.services.comment;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.thesis.controllers.RequestComment;
import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.entities.QCommentData;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.CommentNotFoundException;
import org.choongang.thesis.repositories.CommentDataRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.Sort.Order.asc;

@Service
@RequiredArgsConstructor
public class CommentInfoService {
    private final CommentDataRepository commentDataRepository;
    private final ThesisRepository thesisRepository;
    private final MemberUtil memberUtil;

    /**
     * 댓글 단일 조회
     * @param seq : 댓글 번호
     * @return
     */
    public CommentData get(Long seq) {
        CommentData data = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);

        addCommentInfo(data);

        return data;
    }
    public RequestComment getForm(Long seq) {
        CommentData data = get(seq);
        RequestComment form = new ModelMapper().map(data, RequestComment.class);

        form.setTid(data.getThesis().getTid());

        return form;
    }

    /**
     * 논문별 댓글 목록 조회
     * @param tid
     * @return
     */
    public List<CommentData> getList(Long tid) {
        QCommentData commentData = QCommentData.commentData;
        BooleanBuilder andBuilder = new BooleanBuilder();
        andBuilder.and(commentData.thesis.tid.eq(tid));

        List<CommentData> items = (List<CommentData>)commentDataRepository.findAll(andBuilder, Sort.by(asc("createdAt")));

        items.forEach(this::addCommentInfo);

        return items;
    }

    /**
     * 댓글 추가 정보 처리
     * @param data
     */
    private void addCommentInfo(CommentData data) {
        boolean editable = false, mine = false;

        Member member = memberUtil.getMember(); //댓글을 작성한 회원

         //관리자는 댓글 수정, 삭제 제한 없음
        if (memberUtil.isAdmin()) {
            editable = true;
        }

        //회원이 작성한 댓글이면 현재 로그인 사용자의 아이디와 동일해야 수정, 삭제 가능
        if (member != null && memberUtil.isLogin() && member.getEmail().equals(data.getEmail())) {
            editable =  mine = true;
        }

        // 수정 버튼 노출 여부
        // 관리자 - 노출, 회원 게시글 - 직접 작성한 게시글
        boolean showEdit = memberUtil.isAdmin() || mine || member == null;

        data.setShowEdit(showEdit);

        data.setEditable(editable);
        data.setMine(mine);
    }

    //논문별 댓글 수 업데이트
    public void updateCommentCount(Long tid) {
        Thesis data = thesisRepository.findById(tid).orElse(null);
        if (data == null) {
            return;
        }

        int total = commentDataRepository.getTotal(tid);

        data.setCommentCount(total);

        thesisRepository.flush();

    }
}
