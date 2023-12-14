package com.pro06.dto;

import com.pro06.entity.Book;
import com.pro06.entity.BookImg;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BookVO {
    private Book book;
    private BookImg fileList;
}
