package com.pro06.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LectureVO {
    private LectureDto lecture;
    private List<VideoDto> fileList;
    private LecTestDto lecTest;
    private Integer cno;
}
