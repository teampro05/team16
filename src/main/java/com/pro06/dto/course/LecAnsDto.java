package com.pro06.dto.course;

import com.pro06.dto.BaseDto;
import com.pro06.dto.course.CourseDto;
import com.pro06.dto.course.LectureDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LecAnsDto extends BaseDto {

    @NotBlank
    private Integer no;         // 시험 번호

    @Size(max = 20)
    @NotBlank
    private String id;
    
    // 제출된 답안
    @Size(max = 255)
    @NotBlank
    private String answer1;         // 1번

    @Size(max = 255)
    @NotBlank
    private String answer2;         // 2번

    @Size(max = 255)
    @NotBlank
    private String answer3;         // 3번

    @Size(max = 255)
    @NotBlank
    private String answer4;         // 4번

    @Size(max = 255)
    @NotBlank
    private String answer5;         // 5번

    @NotBlank
    private Integer ansCnt;         // 맞은 개수

    @NotNull
    private CourseDto course;        // 강좌 번호 외래키 지정

    @NotNull
    private LectureDto lecture;        // 강의 번호 외래키 지정
}
