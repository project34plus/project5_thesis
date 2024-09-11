package org.choongang.thesis.services;

import lombok.RequiredArgsConstructor;
import org.choongang.file.services.FileUploadDoneService;
import org.choongang.member.MemberUtil;
import org.choongang.member.entities.Member;
import org.choongang.thesis.constants.Category;
import org.choongang.thesis.controllers.RequestThesis;
import org.choongang.thesis.controllers.ThesisApprovalRequest;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.entities.VersionLog;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.FieldRepository;
import org.choongang.thesis.repositories.ThesisRepository;
import org.choongang.thesis.repositories.VersionLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ThesisSaveService {
    private final ThesisRepository thesisRepository;
    private final FieldRepository fieldRepository;
    private final FileUploadDoneService uploadDoneService;
    private final VersionLogRepository versionLogRepository;

    private final MemberUtil memberUtil;

    public void save(RequestThesis form) {

        String mode = Objects.requireNonNullElse(form.getMode(), "register");
        Long tid = form.getTid();

        Thesis thesis = null;
        if (mode.equals("update") && tid != null) { // 수정
            thesis = thesisRepository.findById(tid).orElseThrow(ThesisNotFoundException::new);
            //수정 전 논문상태
            String beforeState = thesis.toString();


            saveVersion(thesis, form.getMajorVersion(), form.getMinorVersion(), beforeState, form.toString());
        } else { // 추가
            thesis = new Thesis();
            thesis.setGid(form.getGid());

            if (memberUtil.isLogin()) {
                Member member = memberUtil.getMember();
                thesis.setEmail(member.getEmail());
                thesis.setUserName(member.getUserName());
                thesis.setApproval(false);
            }
            saveVersion(thesis, 1, 0,null, form.toString());
        }

        /* 추가, 수정 공통 처리 S */
        thesis.setCategory(Category.valueOf(form.getCategory()));
        thesis.setPoster(form.getPoster());
        thesis.setTitle(form.getTitle());
        thesis.setContributor(form.getContributor());
        thesis.setThAbstract(form.getThAbstract());
        thesis.setReference(form.getReference());
        thesis.setVisible(form.isVisible());

        if (memberUtil.isAdmin()) {
            thesis.setApproval(form.isApproval()); // 승인은 관리자인 경우만 가능
        }

        thesis.setToc(form.getToc());
        thesis.setLanguage(form.getLanguage());
        thesis.setCountry(form.getCountry());

        /* fields 항목 처리 */
        List<String> ids = form.getFields();
        List<Field> fields = null;
        if (ids != null && !ids.isEmpty()) {
            fields = fieldRepository.findAllById(ids);
        }
        thesis.setFields(fields);
        /* 추가, 수정 공통 처리 S */

        thesisRepository.saveAndFlush(thesis);

        // 파일 업로드 완료 처리
        uploadDoneService.process(thesis.getGid());
    }
    private void saveVersion(Thesis thesis, int major, int minor,String beforeState ,String afterState) {
        VersionLog versionLog = VersionLog.builder()
                .thesis(thesis)
                .major(major)
                .minor(minor)
                .before(beforeState)
                .after(afterState)
                .build();
        versionLogRepository.saveAndFlush(versionLog);
    }







    @Transactional
    public void saveTheses(List<ThesisApprovalRequest.ThesisApprovalItem> theses){
        // 관리자 권한 확인
//        if (!memberUtil.isAdmin()) {
//            throw new AdminNotFoundException();
//        }
        List<Thesis> thesisList = new ArrayList<>();

        for (ThesisApprovalRequest.ThesisApprovalItem item : theses) {
            Thesis thesis = thesisRepository.findById(item.getThesisId())
                    .orElseThrow(ThesisNotFoundException::new);
            thesis.setApproval(item.isApproved());
            thesisList.add(thesis);
        }
        thesisRepository.saveAllAndFlush(thesisList);


    }
}