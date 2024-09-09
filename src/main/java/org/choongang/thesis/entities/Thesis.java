package org.choongang.thesis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseMemberEntity;

import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Thesis extends BaseMemberEntity {
    @Id
    @GeneratedValue
    private Long tid;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Field> id;

    @Column(length = 40, nullable = false)
    private String poster; // 작성자

    @Column(nullable = false)
    private String title; // 논문 제목

    private String contributor; // 기여자

    @Lob
    private String thAbstract; // 초록

    @Lob
    private String reference; // 참고 문헌

    @Column(length = 65, nullable = false)
    private String gid; // 그룹 ID
    private boolean visible; // 공개 여부
    private int viewCount; // 조회수

    @Column(length = 80)
    private String publisher; // 발행 기관

    @Lob
    private String toc; // 목차

    @Column(length = 20)
    private String language; // 언어

    @Column(length = 40, nullable = false)
    private String userName; // 회원명

    @Column(length = 80, nullable = false)
    private String email; // 이메일
}
