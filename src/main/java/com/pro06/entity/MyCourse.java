package com.pro06.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

// 나의 학습방 테이블
// 사용자가 수강신청을 하면 여기에 저장되며 강의를 수강할 수 있다.

@Entity
@Getter
@Table(name="mycourse")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor

// column에 defualt 값을 설정할 때에 밑의 두개를 같이 써줘야 한다.
@DynamicInsert
@DynamicUpdate
public class MyCourse extends BaseEntity{

    @Id
    @Column(name = "no")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;         // 번호

    @Column(length = 20, nullable = false)
    private String id;          // 수강신청자

    @Column(length = 20)
    @ColumnDefault("'y'")
    private String state;       // 수강상태

    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JoinColumn(name = "cno", referencedColumnName = "no")
    private Course course;      // 강좌 번호
}
