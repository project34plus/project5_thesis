package org.choongang.thesis.services.comment;

import lombok.RequiredArgsConstructor;
import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.repositories.CommentDataRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentDeleteService {
    private final CommentDataRepository commentDataRepository;
    private final CommentInfoService commentInfoService;

    public Long delete(Long seq) {

        CommentData data = commentInfoService.get(seq);
        Long tid = data.getThesis().getTid();

        commentDataRepository.delete(data);
        commentDataRepository.flush();

        return tid; // 논문 번호를 가지고 와서 반환한다.
    }
}
