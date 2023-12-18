package com.pro06.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class MbookImg {
    private Integer no;
    private Integer Mbno; // MiddleBookNO
    private String savefolder;
    private String originfile;
    private String savefile;
    private Long filesize;
    private String uploaddate;
}
