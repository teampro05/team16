package com.pro06.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class HbookImg {
    private Integer no;
    private Integer Hbno; // HighBookNO
    private String savefolder;
    private String originfile;
    private String savefile;
    private Long filesize;
    private String uploaddate;
}
