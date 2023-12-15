package com.pro06.dto;

import com.pro06.entity.BaseEntity;
import com.pro06.entity.Course;
import com.pro06.entity.Lecture;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LecQueDto {

    private Integer no;         // 강의 질문 번호

    @NotBlank
    private String id;          // 작성자 아이디

    @NotNull
    private String que;         // 작성자의 질문

    private String ans;        // 관리자가 작성한 답안

    @NotNull
    private Integer sec;       // 영상의 시간 (초)

    @NotNull
    private Integer page;       // 영상 위치

    @NotNull
    private CourseDto course;        // 강좌 번호 외래키 지정

    @NotNull
    private LectureDto lecture;        // 강의 번호 외래키 지정
}
