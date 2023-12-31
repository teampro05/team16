package com.pro06.entity.course;

import com.pro06.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Table(name="lecans")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

// column에 defualt 값을 설정할 때에 밑의 두개를 같이 써줘야 한다.
@DynamicInsert
@DynamicUpdate
public class LecAns extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;         // 시험 번호

    @Column(length = 20, nullable = false)
    private String id;          // 시험을 본 유저의 아이디
    
    // 제출된 답안
    @Column(nullable = false)
    private String answer1;         // 1번

    @Column(nullable = false)
    private String answer2;         // 2번

    @Column(nullable = false)
    private String answer3;         // 3번

    @Column(nullable = false)
    private String answer4;         // 4번

    @Column(nullable = false)
    private String answer5;         // 5번

    private Integer ansCnt;         // 맞은 개수
    
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", referencedColumnName = "no")
    private Course course;        // 강좌 번호 외래키 지정

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "lno", referencedColumnName = "no")
    private Lecture lecture;        // 강의 번호 외래키 지정

    public void answerChange(String answer1, String answer2, String answer3, String answer4, String answer5,
                             Integer ansCnt){
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.answer3 = answer3;
        this.answer4 = answer4;
        this.answer5 = answer5;
        this.ansCnt = ansCnt;
    }
}
