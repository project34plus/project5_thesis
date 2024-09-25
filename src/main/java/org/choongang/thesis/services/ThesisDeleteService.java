package org.choongang.thesis.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileDeleteService;
import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ThesisDeleteService {
    private final ThesisRepository thesisRepository;
    private final FieldRepository fieldRepository;
    private final FileDeleteService deleteService;
    private final CommentDataRepository commentRepository;
    private final ThesisViewRepository thesisViewRepository;
    private final ThesisViewDailyRepository thesisViewDailyRepository;
    private final VersionLogRepository versionLogRepository;
    private final VisitHistoryRepository visitHistoryRepository;
    private final WishListRepository wishListRepository;


    @Transactional
    public void delete(Long tid) {
        Thesis thesis = thesisRepository.findById(tid)
                .orElseThrow(ThesisNotFoundException::new);

        // 1. Thesis와 연결된 Field에서 연결 제거
        List<Field> fields = thesis.getFields();
        if (fields != null && !fields.isEmpty()) {
            for (Field field : fields) {
                field.getTheses().remove(thesis);
            }
            fieldRepository.deleteAll(fields);
        }

        // 2. Thesis와 연결된 CommentData 삭제
        List<CommentData> comments = thesis.getComments();
        if (comments != null && !comments.isEmpty()) {
            commentRepository.deleteAll(comments);
        }

        // 3. Thesis와 연결된 ThesisView 삭제
        thesisViewRepository.deleteByTid(tid); // tid로 ThesisView 삭제하는 메소드

        // 4. Thesis와 연결된 ThesisViewDaily 삭제
        thesisViewDailyRepository.deleteByTid(tid); // tid로 ThesisViewDaily 삭제하는 메소드

        // 5. Thesis와 연결된 VersionLog 삭제
        versionLogRepository.deleteByThesis(thesis); // Thesis로 VersionLog 삭제하는 메소드

        // 6. Thesis와 연결된 VisitHistory 삭제
        visitHistoryRepository.deleteByThesis(thesis); // Thesis로 VisitHistory 삭제하는 메소드

        // 7. Thesis와 연결된 WishList 삭제
        wishListRepository.deleteByTid(tid); // tid로 WishList 삭제하는 메소드

        // 8. 논문 PDF 파일 삭제
        String gid = thesis.getGid();
        deleteService.delete(gid);

        // 9. 논문 삭제
        thesisRepository.delete(thesis);
    }
    public void deleteList(List<Long> tids) {
        for(Long tid : tids) {
            Thesis thesis = thesisRepository.findById(tid).orElseThrow(ThesisNotFoundException::new);
            //학문 분야 분류 삭제
            List<Field> fields = thesis.getFields();
            if (fields != null && !fields.isEmpty()) {
                fieldRepository.deleteAll(fields);
            }
            // 논문 PDF 파일 삭제
            String gid = thesis.getGid();
            deleteService.delete(gid);

            // 논문 삭제
            thesisRepository.delete(thesis);
        }
        thesisRepository.flush();
    }
}
