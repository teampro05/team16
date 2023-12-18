package com.pro06.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter @Setter
@ToString
public class BoardDTO {

    private Integer no;

    @Column(length = 100)
    @NotEmpty(message = "제목은 필수 입력 값입니다.")
    private String title;

    @Column(length = 1000)
    @NotEmpty(message = "내용은 필수 입력 값입니다.")
    private String content;

    @NotEmpty(message = "저자는 필수 입력 값입니다.")
    private String author;

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;


}