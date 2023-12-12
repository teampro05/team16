package com.pro06.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Table(name="lectest")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

// column에 defualt 값을 설정할 때에 밑의 두개를 같이 써줘야 한다.
@DynamicInsert
@DynamicUpdate
public class LecTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;         // 시험 번호
    
    // 문제 + 4지선다
    @Column(length = 100, nullable = false)
    private String exam1;

    @Column(length = 100, nullable = false)
    private String exam2;

    @Column(length = 100, nullable = false)
    private String exam3;

    @Column(length = 100, nullable = false)
    private String exam4;

    @Column(length = 100, nullable = false)
    private String exam5;
    
    // 해당 문제의 답안
    @Column(length = 100, nullable = false)
    private String answer1;

    @Column(length = 100, nullable = false)
    private String answer2;

    @Column(length = 100, nullable = false)
    private String answer3;

    @Column(length = 100, nullable = false)
    private String answer4;

    @Column(length = 100, nullable = false)
    private String answer5;
    
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", referencedColumnName = "no")
    private Course course;        // 강좌 번호 외래키 지정

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "lno", referencedColumnName = "no")
    private Lecture lecture;        // 강의 번호 외래키 지정
}
