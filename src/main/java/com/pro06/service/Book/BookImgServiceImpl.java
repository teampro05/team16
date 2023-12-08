package com.pro06.service.Book;

import com.pro06.repository.Book.BookImgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookImgServiceImpl {
    @Autowired
    private BookImgRepository bookImgRepository;

    public List<String> bookImgList(Integer bno) {
        return bookImgRepository.bookImgList(bno);
    }
}
