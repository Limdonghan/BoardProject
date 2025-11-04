package com.example.BoardProject_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportsreasons")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ReportReasonEntity {  // 신고 상태 테이블

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false,  length = 50, unique = true)
    private String code;  // 내부 식별 코드

    @Column(name = "code_name", nullable = false,  length = 100)
    private String codeName;  // 코드 이름

    @Column(nullable = false)
    private String description;  // 코드 설명

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;  // 생성날짜
}
