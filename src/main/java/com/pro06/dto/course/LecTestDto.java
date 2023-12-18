package com.pro06.dto.course;

import com.pro06.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LecTestDto extends BaseDto {

    @NotBlank
    private Integer no;         // 시험 번호
    
    // 문제 + 4지선다
    @Size(max = 255)
    @NotBlank
    private String exam1;

    @Size(max = 255)
    @NotBlank
    private String exam2;

    @Size(max = 255)
    @NotBlank
    private String exam3;

    @Size(max = 255)
    @NotBlank
    private String exam4;

    @Size(max = 255)
    @NotBlank
    private String exam5;
    
    // 해당 문제의 답안
    @Size(max = 255)
    @NotBlank
    private String answer1;

    @Size(max = 255)
    @NotBlank
    private String answer2;

    @Size(max = 255)
    @NotBlank
    private String answer3;

    @Size(max = 255)
    @NotBlank
    private String answer4;

    @Size(max = 255)
    @NotBlank
    private String answer5;

    @NotNull
    private CourseDto course;        // 강좌 번호 외래키 지정

    @NotNull
    private LectureDto lecture;        // 강의 번호 외래키 지정
}
