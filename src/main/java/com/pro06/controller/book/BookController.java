package com.pro06.controller.book;

import com.pro06.entity.Book;
import com.pro06.entity.BookImg;
import com.pro06.service.Book.BookImgService;
import com.pro06.service.Book.BookImgServiceImpl;
import com.pro06.service.Book.BookServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/book/*")
public class BookController {
    @Autowired
    private BookServiceImpl bookService;

    @Autowired
    private BookImgServiceImpl bookImgService;

//    @Value("${spring.servlet.multipart.location}") // properties 파일에서 값을 가져오도록 수정
//    private String uploadFolder;
//
//    // 생성자를 통해 값을 할당
//    public BookController(@Value("${c:\\pro06}") String uploadFolder) {
//        this.uploadFolder = uploadFolder;
//    }

    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @GetMapping("bookList")
    public String bookList(Model model) throws Exception {
        List<Book> bookList = bookService.bookList();
//        List<FileDTO> fileList = new ArrayList<>();
//        for (Product pro:productList) {
//            FileDTO dto = productService.thmbn(pro.getNo());
//            fileList.add(dto);
//        }
//        log.info(fileboardList.toString());
        model.addAttribute("bookList", bookList);
//        model.addAttribute("fileList", fileList);
        return "book/bookList";
    }

    @GetMapping("getBook")
    public String getBook(@RequestParam("bno") int bno, Model model, HttpServletRequest req) throws Exception {
//        FileVO fileboard = new FileVO();

//        HttpSession session = req.getSession();
//        Cookie[] cookieFromRequest = req.getCookies();
//        String cookieValue = null;
//        for(int i = 0 ; i<cookieFromRequest.length; i++) {
//            // 요청정보로부터 쿠키를 가져온다.
//            cookieValue = cookieFromRequest[0].getValue();  // 테스트라서 추가 데이터나 보안사항은 고려하지 않으므로 1번째 쿠키만 가져옴
//        }

//        // 쿠키 세션 입력
//        if (session.getAttribute(no+":cookieFile") == null) {
//            session.setAttribute(no+":cookieFile", no + ":" + cookieValue);
//        } else {
//            session.setAttribute(no+":cookieFile ex", session.getAttribute(no+":cookieFile"));
//            if (!session.getAttribute(no+":cookieFile").equals(no + ":" + cookieValue)) {
//                session.setAttribute(no+":cookieFile", no + ":" + cookieValue);
//            }
//        }
//
//        // 쿠키와 세션이 없는 경우 조회수 카운트
//        if (!session.getAttribute(no+":cookieFile").equals(session.getAttribute(no+":cookieFile ex"))) {
//            productService.countUp(no);
//        }

        Book book = bookService.getBook(bno);
//        List<FileDTO> fileList = productService.getFileGroupList(no); // 해당 게시글의 파일 목록
//        fileboard.setFileBoard(product);
//        fileboard.setFileList(fileList);

        List<BookImg> bookImgList = bookImgService.bookImgList(bno);

//        model.addAttribute("product", fileboard.getFileBoard());
//        model.addAttribute("fileList", fileboard.getFileList());
        model.addAttribute("book", book);
        model.addAttribute("bookImg", bookImgList);
        return "book/getBook";
    }

}
