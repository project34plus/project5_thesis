package org.choongang.thesis.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class VisitHistory {
    @Id
    @GeneratedValue
    private Long seq;

    @CreatedBy
    @Column(length=80, nullable = false)
    private String email;

    @CreatedDate
    private LocalDateTime searchDate; // 검색일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="tid")
    @JsonIgnore
    private Thesis thesis;
}
