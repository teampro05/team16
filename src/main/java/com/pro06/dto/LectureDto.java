package com.pro06.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 강의 테이블
// 강좌에 속해있으며 여러개의 동영상을 하나로 묶는 역할

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LectureDto extends BaseDto {

    @NotBlank
    private Integer no;         // 강의 번호

    @Size(max = 20)
    @NotBlank
    private String id;          // 작성자(관리자)

    @Size(max = 100)
    @NotBlank
    private String title;       // 강의 제목

    @Size(max = 2000)
    @NotNull
    private String content;     // 강의 설명

    @Size(max = 100)
    @NotNull
    private String keyword;     // 키워드

    @NotNull
    private CourseDto course;        // 강좌 번호 외래키 지정
}
