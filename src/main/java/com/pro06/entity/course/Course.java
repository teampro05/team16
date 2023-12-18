package com.pro06.entity.course;

import com.pro06.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

// 강좌 테이블
// 여기에 나중에 선생님 관련 컬럼 하나 추가
// validation을 이용해 size, notnull 을 써도 되고 아니면 
// column을 이용해 length랑 null able 지정해줘도 됨

@Entity
@Getter
@Table(name="course")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

// column에 defualt 값을 설정할 때에 밑의 두개를 같이 써줘야 한다.
@DynamicInsert
@DynamicUpdate
public class Course extends BaseEntity {

    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;         // 강좌 번호

    @Comment("작성자(관리자)")    // jpa 코멘트 작성 예시
    @Column(length = 20, nullable = false)
    private String id;          // 작성자(관리자)

    @Column(length = 10, nullable = false)
    private String level;      // 학년

    @Column(length = 200, nullable = false)
    private String title;       // 제목

    @Column(length = 2000, nullable = false)
    private String content;     // 내용

    @ColumnDefault("0")
    private Integer cnt;        // 조회수

    @ColumnDefault("0")
    private Integer peo;        // 수강인원

    @Column(nullable = false)
    private Integer peo_max;    // 최대 수강인원

    @Column(nullable = true)
    private LocalDateTime copendate;    // 개강날짜

    private Integer copen;    // 오픈여부

    public void peoUp(){
        this.peo = this.peo + 1;
    }

    public void change(String id, String level,
                       String title, String content, Integer copen) {
        this.id = id;
        this.level = level;
        this.title = title;
        this.content = content;
        this.copen = copen;
    }

}
