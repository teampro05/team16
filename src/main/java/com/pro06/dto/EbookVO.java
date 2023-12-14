package com.pro06.dto;

import com.pro06.entity.Ebook;
import com.pro06.entity.EbookImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class EbookVO {
    private Ebook fileBoard;
    private List<EbookImg> fileList;
}
