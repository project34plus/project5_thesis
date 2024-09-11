package org.choongang.thesis.services;

import com.querydsl.core.BooleanBuilder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.choongang.file.entities.FileInfo;
import org.choongang.file.services.FileInfoService;
import org.choongang.global.ListData;
import org.choongang.global.Pagination;
import org.choongang.member.MemberUtil;
import org.choongang.thesis.constants.ApprovalStatus;
import org.choongang.thesis.constants.Category;
import org.choongang.thesis.controllers.RequestThesis;
import org.choongang.thesis.controllers.ThesisSearch;
import org.choongang.thesis.entities.Field;
import org.choongang.thesis.entities.QThesis;
import org.choongang.thesis.entities.Thesis;
import org.choongang.thesis.exceptions.ThesisNotFoundException;
import org.choongang.thesis.repositories.ThesisRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.data.domain.Sort.Order.desc;

@Service
@RequiredArgsConstructor
@Transactional
public class ThesisInfoService {
    private final ThesisRepository thesisRepository;
    private final FileInfoService fileInfoService;
    private final HttpServletRequest request;
    private final ModelMapper modelMapper;
    private final MemberUtil memberUtil;


    public Thesis get(Long tid) {
        Thesis item = thesisRepository.findById(tid).orElseThrow(ThesisNotFoundException::new);

//       addInfo(item);

        return item;
    }

    public RequestThesis getForm(Long tid) {
        Thesis item = get(tid);
        RequestThesis form = modelMapper.map(item, RequestThesis.class);
        Category category = item.getCategory();
        form.setCategory(category == null ? null : category.name());

        List<Field> fields = item.getFields();
        if (fields != null && !fields.isEmpty()) {
            List<String> ids = fields.stream().map(Field::getId).toList();
            form.setFields(ids);
        }

        return form;
    }

    /**
     * 논문 목록
     * @param search
     * @return
     */
    public ListData<Thesis> getList(ThesisSearch search) {
        int page = Math.max(search.getPage(), 1);
        int limit = search.getLimit();
        limit = limit < 1 ? 20 : limit;

        LocalDate sDate = search.getSDate();
        LocalDate eDate = search.getEDate();

        /* 검색 처리 S */
        BooleanBuilder andBuilder = new BooleanBuilder();
        QThesis thesis = QThesis.thesis;
        String sopt = search.getSopt();
        String skey = search.getSkey();
        List<String> category = search.getCategory();
        List<String> fields = search.getFields();
        List<String> email = search.getEmail();

        if (search.getApprovalStatus() != null) {
            ApprovalStatus approvalStatus = memberUtil.isAdmin() ? search.getApprovalStatus() : ApprovalStatus.APPROVED;
            andBuilder.and(thesis.approvalStatus.eq(approvalStatus)); // 승인 상태에 따른 필터링
        }

        //작성한 회원 이메일로 조회
        if(email != null && !email.isEmpty()) {
            andBuilder.and(thesis.email.in(email));
        }

        if (!memberUtil.isAdmin()) {
            andBuilder.and(thesis.visible.eq(true))
                    .and(thesis.approvalStatus.eq(ApprovalStatus.APPROVED));

        }

        if(sDate != null) {
            andBuilder.and(thesis.createdAt.after(sDate.atStartOfDay()));
        }
        if(eDate != null) {
            andBuilder.and(thesis.createdAt.before(eDate.atStartOfDay()));
        }


        /* 검색 처리 E */

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(desc("createdAt")));
        Page<Thesis> data = thesisRepository.findAll(andBuilder, pageable);

        long total = data.getTotalElements();
        Pagination pagination = new Pagination(page, (int)total, 10, limit, request);

        List<Thesis> items = data.getContent();
        items.forEach(this::addInfo);

        return new ListData<>(items, pagination);
    }
    public ListData<Thesis> getMyList(ThesisSearch search) {
        if(!memberUtil.isLogin()){
            return new ListData<>();
        }
        String email = memberUtil.getMember().getEmail();
        search.setEmail(List.of(email));
        return getList(search);
    }



    // 추가 정보 처리
    private void addInfo(Thesis item) {
        Category category = item.getCategory();
        item.set_category(category == null ? null : category.getTitle());

        // 논문 파일
        List<FileInfo> files = fileInfoService.getList(item.getGid());
        item.setFileInfo(files == null || files.isEmpty() ? null : files.get(0));

        // 학문 분류 처리
        List<Field> fields = item.getFields();
        Map<String, String[]> _fields = fields == null || fields.isEmpty() ? null : fields.stream().collect(Collectors.toMap(Field::getId, f -> new String[]{f.getName(), f.getSubfield()}));

        item.set_fields(_fields);
    }
}
