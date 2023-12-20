package com.pro06.entity.course;

import com.pro06.dto.course.LecTestDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
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
    
    // 문제
    @Column(nullable = false)
    private String exam1;

    @Column(nullable = false)
    private String exam2;

    @Column(nullable = false)
    private String exam3;

    @Column(nullable = false)
    private String exam4;

    @Column(nullable = false)
    private String exam5;
    
    // 해당 문제의 답안
    @Column(nullable = false)
    private String answer1;

    @Column(nullable = false)
    private String answer2;

    @Column(nullable = false)
    private String answer3;

    @Column(nullable = false)
    private String answer4;

    @Column(nullable = false)
    private String answer5;
    
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", referencedColumnName = "no")
    private Course course;        // 강좌 번호 외래키 지정

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "lno", referencedColumnName = "no")
    private Lecture lecture;        // 강의 번호 외래키 지정

    // 시험 정보 수정에 필요한 메소드
    public void change(LecTestDto dto) {
        this.exam1 = dto.getExam1();
        this.exam2 = dto.getExam2();
        this.exam3 = dto.getExam3();
        this.exam4 = dto.getExam4();
        this.exam5 = dto.getExam5();

        this.answer1 = dto.getAnswer1();
        this.answer2 = dto.getAnswer2();
        this.answer3 = dto.getAnswer3();
        this.answer4 = dto.getAnswer4();
        this.answer5 = dto.getAnswer5();
    }
}
