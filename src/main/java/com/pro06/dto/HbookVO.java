package com.pro06.dto;

import com.pro06.entity.Hbook;
import com.pro06.entity.HbookImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class HbookVO {
    private Hbook fileBoard;
    private List<HbookImg> fileList;
}
