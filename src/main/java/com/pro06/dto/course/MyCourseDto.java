package com.pro06.dto.course;

import com.pro06.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

// 나의 학습방 테이블
// 사용자가 수강신청을 하면 여기에 저장되며 강의를 수강할 수 있다.

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MyCourseDto extends BaseDto {

    @NotBlank
    private Integer no;         // 번호

    @Size(max = 20)
    @NotBlank
    private String id;          // 수강신청자

    @Size(max = 10)
    @NotNull
    private String state;       // 수강상태

    @NotNull
    private CourseDto course;      // 강좌 번호
}
