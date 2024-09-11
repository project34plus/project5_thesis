package org.choongang.thesis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseEntity;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(VisitHistory.class)
public class VisitHistory extends BaseEntity {
    @Id
    private Long tid; //논문아이디

    @Id
    @Column(length=80)
    private String email; //회원 이메일

    private LocalDateTime viewdAt; // 논문 본 시간
}
