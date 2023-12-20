package com.pro06.entity.course;

import com.pro06.dto.course.LectureDto;
import com.pro06.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

// 강의 테이블
// 강좌에 속해있으며 여러개의 동영상을 하나로 묶는 역할

@Entity
@Getter
@Table(name="lecture")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

// column에 defualt 값을 설정할 때에 밑의 두개를 같이 써줘야 한다.
@DynamicInsert
@DynamicUpdate
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;         // 강의 번호

    @Column(length = 20, nullable = false)
    private String id;          // 작성자(관리자)

    @Column(length = 200, nullable = false)
    private String title;       // 강의 제목

    @Column(length = 2000, nullable = false)
    private String content;     // 강의 설명

    @Column(length = 200, nullable = false)
    private String keyword;     // 키워드

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", referencedColumnName = "no")
    private Course course;        // 강좌 번호 외래키 지정

    @ColumnDefault("'n'")
    private String deleteYn;      // 삭제 여부

    // 강의 정보 수정에 필요한 메소드
    public void change(LectureDto dto) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.keyword = dto.getKeyword();
    }

    public void delete(String deleteYn) {
        this.deleteYn = deleteYn;
    }
}
