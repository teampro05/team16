package com.pro06.controller.book;

import com.pro06.service.Book.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/book/*")
public class BookController {
    @Autowired
    private BookServiceImpl bookService;

//    @Value("${c:\\pro06}") // properties 파일에서 값을 가져오도록 수정
//    private String uploadFolder;
//
//    // 생성자를 통해 값을 할당
//    public BookController(@Value("${c:\\pro06}") String uploadFolder) {
//        this.uploadFolder = uploadFolder;
//    }

    @Value("c:\\pro06")
    String uploadFolder;


}
