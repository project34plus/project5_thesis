package org.choongang.thesis.services.comment;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.member.MemberUtil;
import org.choongang.thesis.controllers.RequestComment;
import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.CommentNotFoundException;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.CommentDataRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class CommentSaveService {

    private final CommentDataRepository commentDataRepository;
    private final ThesisRepository thesisRepository;

    private final CommentInfoService commentInfoService;

    private final MemberUtil memberUtil;
    private final HttpServletRequest request;

    public CommentData save(RequestComment form) {

        String mode = form.getMode();
        Long seq = form.getSeq(); // 댓글 번호

        mode = StringUtils.hasText(mode) ? mode : "write";

        CommentData data = null;
        if (mode.equals("update") && seq != null) { // 댓글 수정
            data = commentDataRepository.findById(seq).orElseThrow(CommentNotFoundException::new);

        } else { // 댓글 추가
            data = new CommentData();
            // 논문 번호는 변경 X -> 추가할 때 최초 1번만 반영
            Long tid = form.getTid();

            Thesis thesis = thesisRepository.findById(tid).orElseThrow(ThesisNotFoundException::new);

            data.setThesis(thesis);

            data.setUsername(memberUtil.getMember().getUserName()); // 추가할 때 최초 1번만 반영
            data.setEmail(memberUtil.getMember().getEmail());

            data.setIp(request.getRemoteAddr());
            data.setUa(request.getHeader("User-Agent"));
        }

        String commenter = form.getUsername();
        if (StringUtils.hasText(commenter)) {
            data.setUsername(commenter);
        }

        data.setContent(form.getContent());

        commentDataRepository.saveAndFlush(data);

        commentInfoService.updateCommentCount(data.getThesis().getTid());

        return data;
    }

}
