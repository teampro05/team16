package com.pro06.service.Book;

import com.pro06.entity.Book;
import com.pro06.entity.BookImg;

import java.util.List;

public interface BookImgService {

    List<BookImg> bookImgList(Integer bno);
    BookImg getBookImg(Integer no);
    void insertBookImg(BookImg bookImg);
    void deleteBookImg(BookImg bookImg);
}
