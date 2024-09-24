package org.choongang.thesis.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = @Index(name="idx_comment_basic", columnList = "createdAt ASC"))
public class CommentData extends BaseEntity {
    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    private Thesis thesis;

    @Column(length = 80, nullable=false)
    private String email; // 로그인 회원 이메일

    @Column(length = 40, nullable=false)
    private String username; //로그인 회원명 = 작성자

    @Lob
    @Column(nullable = false)
    private String content; //댓글 내용

    @Column(length=20)
    private String ip; //작성자 IP 주소

    @Column(length=150)
    private String ua; //작성자 User-Agent 정보

    @Transient
    private boolean editable; // 수정, 삭제 가능 여부

    @Transient
    private boolean mine; // 소유자

    @Transient
    private boolean showEdit; // 수정, 삭제 버튼 노출 여부
}
