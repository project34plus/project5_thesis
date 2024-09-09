package org.choongang.thesis.entities;

import jakarta.persistence.*;
import lombok.*;
import org.choongang.global.entities.BaseEntity;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentData extends BaseEntity {

    @Id @GeneratedValue
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Thesis thesis;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(length = 20)
    private String ip;
    @Column(length = 150)
    private String ua;
}
