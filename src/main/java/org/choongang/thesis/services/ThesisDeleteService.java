package org.choongang.thesis.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileDeleteService;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ThesisDeleteService {
    private final ThesisRepository thesisRepository;
    private final FieldRepository fieldRepository;
    private final FileDeleteService fileDeleteService;

    public void delete(Long tid) {
        Thesis thesis = thesisRepository.findById(tid).orElseThrow(ThesisNotFoundException::new);

        // 학문 분야 분류 삭제
        List<Field> fields = thesis.getFields();
        if (fields != null && !fields.isEmpty()) {
            fieldRepository.deleteAll(fields);
            fieldRepository.flush();
        }
        // 논문 pdf 파일 삭제
        String gid = thesis.getGid();
        fileDeleteService.delete(gid);

        // 논문 삭제
        thesisRepository.delete(thesis);
        thesisRepository.flush();

    }
}
