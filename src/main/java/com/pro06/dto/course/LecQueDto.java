package com.pro06.dto.course;

import com.fasterxml.jackson.databind.ser.Serializers;
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
public class LecQueDto extends BaseDto {

    private Integer no;         // 강의 질문 번호

    @Size(max = 20)
    @NotBlank
    private String id;          // 작성자 아이디

    @Size(max = 1000)
    @NotNull
    private String que;         // 작성자의 질문

    @Size(max = 2000)
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
