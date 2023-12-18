package com.pro06.dto;

import com.pro06.entity.Tbook;
import com.pro06.entity.TbookImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class TbookVO {
    private Tbook fileBoard;
    private List<TbookImg> fileList;
}
