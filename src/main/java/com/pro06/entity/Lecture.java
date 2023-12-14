package com.pro06.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

// 강의 테이블
// 강좌에 속해있으며 여러개의 동영상을 하나로 묶는 역할

@Entity
@Getter
@Setter
@Table(name="lecture")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

// column에 defualt 값을 설정할 때에 밑의 두개를 같이 써줘야 한다.
@DynamicInsert
@DynamicUpdate
public class Lecture extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;         // 강의 번호

    @Column(length = 20, nullable = false)
    private String id;          // 작성자(관리자)

    @Column(length = 100, nullable = false)
    private String title;       // 강의 제목

    @Column(length = 2000, nullable = false)
    private String content;     // 강의 설명

    @Column(length = 100, nullable = false)
    private String keyword;     // 키워드

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", referencedColumnName = "no")
    private Course course;        // 강좌 번호 외래키 지정
}
