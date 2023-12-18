package com.pro06.dto;

import com.pro06.entity.Mbook;
import com.pro06.entity.MbookImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class MbookVO {
    private Mbook fileBoard;
    private List<MbookImg> fileList;
}
