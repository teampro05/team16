package com.pro06.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Hbook {
    private Integer no;   // 교재 코드

    private String id; // 관리자
    private String title; // 제목
    private String content; // 내용
    private String servecontent; // 요약 내용
    private Integer price; // 가격
    private String publish; // 출간일
    private String deleteyn; // 삭제 여부
}
