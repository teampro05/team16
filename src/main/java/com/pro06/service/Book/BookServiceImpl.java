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
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService{

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookImgRepository bookImgRepository;

    @Override
    public List<Book> bookList() {
        return bookRepository.bookList();
    }

    @Override
    public Book getBook(Integer bno) {
        return bookRepository.getBook(bno);
    }

    @Override
    public void insertBook(Book book) {
        bookRepository.save(book);
    }

    @Override
    public void updateBook(Book book) {
        Book bookOptional = bookRepository.getBook(book.getNo());
        Book updatebook = Book.builder()
                .no(bookOptional.getNo())
                .id(bookOptional.getId())
                .title(book.getTitle())
                .content(book.getContent())
                .servecontent(book.getServecontent())
                .price(book.getPrice())
                .publish(book.getPublish())
                .build();
        bookRepository.save(updatebook);
    }
}

