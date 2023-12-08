package com.pro06.service.Book;

import com.pro06.dto.BookVO;
import com.pro06.entity.Book;
import com.pro06.entity.BookImg;
import com.pro06.repository.Book.BookImgRepository;
import com.pro06.repository.Book.BookRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookImgRepository bookImgRepository;

    public void BookVoInsert(BookVO bookVO) throws Exception {
        Book book = bookVO.getBook();
        List<BookImg> fileList = bookVO.getFileList();

        for (BookImg bookImg: fileList) {
            bookImg.setBook(book);
            bookImgRepository.save(bookImg);
        }
    }

    public List<Book> bookList1(Integer bno) { return bookRepository. bookList1(bno); }

    public Book getBook(Integer bno) {
        return bookRepository.bookList2(bno);
    }
}

