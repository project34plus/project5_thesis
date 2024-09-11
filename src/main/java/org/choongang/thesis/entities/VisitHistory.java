package org.choongang.thesis.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.choongang.global.entities.BaseEntity;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisitHistory extends BaseEntity {
    @Id
    private Long tid; //논문아이디

    @Id
    @Column(length=80)
    private String email; //회원 이메일
}
