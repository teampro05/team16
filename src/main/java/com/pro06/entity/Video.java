package com.pro06.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

// 강의 영상 테이블
// 강의 영상 정보를 저장

@Entity
@Getter
@Setter
@Table(name="video")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

// column에 defualt 값을 설정할 때에 밑의 두개를 같이 써줘야 한다.
@DynamicInsert
@DynamicUpdate
public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;             // 영상번호

    @Column(nullable = false)
    private String savefolder;      // 저장경로

    @Column(nullable = false)
    private String originfile;      // 실제 파일 이름

    @Column(nullable = false)
    private String savefile;        // 저장된 파일 이름

    @Column(nullable = false)
    private Long filesize;          // 파일 크기

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", referencedColumnName = "no")
    private Course course;        // 강좌 번호 외래키 지정

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "lno", referencedColumnName = "no")
    private Lecture lecture;        // 강의 번호 외래키 지정
}
