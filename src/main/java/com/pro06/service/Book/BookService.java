package com.pro06.service.Book;

import com.pro06.dto.BookVO;
import com.pro06.entity.Book;
import com.pro06.entity.BookImg;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookService {
    List<Book> bookList();
    Book getBook(Integer bno);
    void insertBook(Book book);
    void updateBook(Book book);

}
