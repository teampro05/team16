package com.pro06.dto.course;

import com.pro06.dto.BaseDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

// 강의 ot 영상
// 강의 ot 영상 정보를 저장

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourseVideoDto extends BaseDto {

    @NotBlank
    private Integer no;             // 영상번호

    @Size(max = 255)
    @NotBlank
    private String savefolder;      // 저장경로

    @Size(max = 255)
    @NotBlank
    private String originfile;      // 실제 파일 이름

    @Size(max = 255)
    @NotBlank
    private String savefile;        // 저장된 파일 이름

    @Size(max = 255)
    @NotBlank
    private Long filesize;          // 파일 크기

    @NotNull
    private CourseDto course;        // 강좌 번호 외래키 지정
}
