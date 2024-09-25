package org.choongang.thesis.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileDeleteService;
import org.choongang.thesis.entities.CommentData;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.CommentDataRepository;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.ThesisRepository;
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

    // 필드와 논문의 관계만 삭제하고 필드는 유지하는 메서드
    private void removeThesisFromFields(Thesis thesis) {
        List<Field> fields = thesis.getFields();
        if (fields != null && !fields.isEmpty()) {
            for (Field field : fields) {
                field.getTheses().remove(thesis); // 필드에서 논문 관계 제거
            }
            fieldRepository.saveAll(fields); // 필드를 다시 저장 (논문 관계가 제거된 상태로)
        }
    }

    public void delete(Long tid) {
        Thesis thesis = thesisRepository.findById(tid)
                .orElseThrow(ThesisNotFoundException::new);

        // 1. 필드와 논문의 관계 제거
        removeThesisFromFields(thesis);

        // 2. Thesis와 연결된 CommentData 삭제
        List<CommentData> comments = thesis.getComments();
        if (comments != null && !comments.isEmpty()) {
            commentRepository.deleteAll(comments);
        }

        // 3. 논문 PDF 파일 삭제
        String gid = thesis.getGid();
        deleteService.delete(gid);

        // 4. 논문 삭제
        thesisRepository.delete(thesis);
    }

    public void deleteList(List<Long> tids) {
        for (Long tid : tids) {
            Thesis thesis = thesisRepository.findById(tid)
                    .orElseThrow(ThesisNotFoundException::new);

            // 1. 필드와 논문의 관계 제거
            removeThesisFromFields(thesis);

            // 2. 논문 PDF 파일 삭제
            String gid = thesis.getGid();
            deleteService.delete(gid);

            // 3. 논문 삭제
            thesisRepository.delete(thesis);
        }

        // flush to ensure all deletions are persisted
        thesisRepository.flush();
    }
}