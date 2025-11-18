package com.example.BoardProject_back.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickName;  // 유저 닉네임

    @Column(nullable = false, length = 100, unique = true)
    private String email;  // 유저 이메일

    @Column(name = "passwd", nullable = false, length = 300)
    private String password;  // 유저 비밀번호

    @ManyToOne(fetch = FetchType.LAZY) // grade_id 컬럼 (N:1 관계)
    @JoinColumn(name = "grade_id")
    private GradeEntity grade;  // 유저 등급

    @ColumnDefault("0")
    private int point;  // 유저 포인트

    @Enumerated(EnumType.STRING)
    @Column(name = "is_role", nullable = false)
    private UserRole userRole;  // 유저 권한

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt; // 생성 날짜

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt; // 수정 날짜

    public void userUpdate(String nickName){
        this.nickName = nickName;
    }
}
