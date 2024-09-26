package org.choongang.thesis.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileDeleteService;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.CommentDataRepository;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ThesisDeleteService {
    private final ThesisRepository thesisRepository;
    private final FieldRepository fieldRepository;
    private final FileDeleteService deleteService;
    private final CommentDataRepository commentRepository;


    @Transactional
    public void delete(Long tid) {
        Thesis thesis = thesisRepository.findById(tid)
                .orElseThrow(ThesisNotFoundException::new);

        thesis.setDeletedAt(LocalDateTime.now());
        thesisRepository.save(thesis);

        // 논문 PDF 파일 삭제
        String gid = thesis.getGid();
        deleteService.delete(gid);
    }
    public void deleteList(List<Long> tids) {
        for(Long tid : tids) {
            Thesis thesis = thesisRepository.findById(tid).orElseThrow(ThesisNotFoundException::new);

            thesis.setDeletedAt(LocalDateTime.now());
            thesisRepository.save(thesis);

            // 논문 PDF 파일 삭제
            String gid = thesis.getGid();
            deleteService.delete(gid);
        }
        thesisRepository.flush();
    }
}
