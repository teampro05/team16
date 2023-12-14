package com.pro06.dto;

import com.pro06.entity.LecTest;
import com.pro06.entity.Lecture;
import com.pro06.entity.Video;
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
    private Lecture lecture;
    private List<Video> fileList;
    private LecTest lecTest;
    private Integer cno;
}
