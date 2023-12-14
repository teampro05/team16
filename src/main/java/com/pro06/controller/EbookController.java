package com.pro06.controller;


import com.pro06.dto.EbookVO;
import com.pro06.entity.Ebook;
import com.pro06.entity.EbookImg;
import com.pro06.service.Book.EBookServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Controller
@Slf4j
//@CrossOrigin("http://localhost:8085")
@RequestMapping("/Ebook/*")
public class EbookController {

    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private EBookServiceImpl eBookService;

    @GetMapping("EbookList")
    public String EbookList(Model model) throws Exception{
        List<Ebook> ebookList = eBookService.EbookList();

        List<EbookVO> voList = new ArrayList<>();
        for (Ebook ebook:ebookList) {
            EbookVO vo = new EbookVO();
            vo.setFileBoard(ebook);
            EbookImg ebookImg = eBookService.thmbn(ebook.getNo());
            List<EbookImg> fileList = new ArrayList<>();
            fileList.add(ebookImg);
            vo.setFileList(fileList);
            voList.add(vo);
        }
        model.addAttribute("EbookList", voList);

        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" + ebookList);
        return "Ebook/EbookList";
    }

    @GetMapping("getEbook")
    public String getFileboard(@RequestParam("no") int no, Model model) throws Exception{
        EbookVO fileboard = new EbookVO();

        Ebook ebook = eBookService.getEbook(no);
        List<EbookImg> fileList = eBookService.getFileGroupList(no);
        fileboard.setFileBoard(ebook);
        fileboard.setFileList(fileList);

        model.addAttribute("Ebook", fileboard);
        model.addAttribute("fileList", fileboard.getFileList());

        return "Ebook/getEbook";
    }

    @GetMapping("EbookInsert")
    public String EbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "Ebook/EbookInsert";
    }

    @GetMapping("fileUpload")
    public String fileUploadForm() {
        return "Ebook/EbookInsert";
    }

    @PostMapping("fileUpload")
    public String fileUpload(@RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Ebook ebook = new Ebook();
        // ebook.setNo(params.get("no"));
        ebook.setId(params.get("id"));
        ebook.setTitle(params.get("title"));
        ebook.setContent(params.get("content"));
        ebook.setServecontent(params.get("servecontent"));
        ebook.setPrice(Integer.valueOf(params.get("price")));
        ebook.setPublish(params.get("publish"));

/*
        // 가격에 대한 String 값을 BigDecimal로 변환
        String priceString = params.get("price");
        BigDecimal price = new BigDecimal(priceString);
        ebook.setPrice(price);

        // 출간일에 대한 String 값을 LocalDate로 변환
        String publishString = params.get("publish");
        LocalDate publish = LocalDate.parse(publishString);
        ebook.setPublish(publish);
*/


        File folder = new File(uploadFolder);
        if (!folder.exists())
            folder.mkdirs();
        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);

        //여러 파일 반복 저장
        List<EbookImg> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                EbookImg data = new EbookImg();
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());
                Date today = new Date();
                data.setUploaddate(today.toString());
                fileList.add(data);

                // 파일 저장
                File saveFile = new File(uploadFolder, saveFileName);
                try {
                    file.transferTo(saveFile);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            }
        }

        EbookVO fileboard = new EbookVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(ebook);
        eBookService.insertFileboard(fileboard);
        return "redirect:/Ebook/EbookList";
    }

    @GetMapping("EbookDelete")
    public String EbookDelete(@RequestParam("no") Integer postNo, HttpServletRequest req) throws Exception {

        //실제 파일 삭제 로직
        //파일 경로 지정

        List<EbookImg> fileList = eBookService.getFileGroupList(postNo);
        for (EbookImg ebookImg : fileList) {
            File file = new File(uploadFolder + "/" + ebookImg.getSavefile());
            if (file.exists()) { // 해당 파일이 존재하면
                file.delete(); // 파일 삭제
            }
        }
        //데이터베이스의 파일 자료실과 파일의 내용 삭제
        int ck = eBookService.removeFileboard(postNo);
        return "redirect:/Ebook/EbookList";
    }

    // 거래글 수정폼 이동
    @GetMapping("EbookUpdate")
    public String modifyFileboard(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Ebook ebook = eBookService.getEbook(postNo);
        List<EbookImg> fileList = eBookService.getFileGroupList(postNo);
        model.addAttribute("ebook", ebook);
        model.addAttribute("fileList", fileList);
        return "/Ebook/EbookUpdate";
    }

    @PostMapping("productUpdate")
    public String fileUpload(@RequestParam("Ebno") Integer postNo,
                             @RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Ebook ebook = new Ebook();
        // ebook.setNo(params.get("no"));
        ebook.setId(params.get("id"));
        ebook.setTitle(params.get("title"));
        ebook.setContent(params.get("content"));
        ebook.setServecontent(params.get("servecontent"));
        ebook.setPrice(Integer.valueOf(params.get("price")));
        ebook.setPublish(params.get("publish"));

/*
        // 가격에 대한 String 값을 BigDecimal로 변환
        String priceString = params.get("price");
        BigDecimal price = new BigDecimal(priceString);
        ebook.setPrice(price);

        // 출간일에 대한 String 값을 LocalDate로 변환
        String publishString = params.get("publish");
        LocalDate publish = LocalDate.parse(publishString);
        ebook.setPublish(publish);
*/

        File folder = new File(uploadFolder);
        if (!folder.exists())
            folder.mkdirs();
        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);

        //여러 파일 반복 저장
        List<EbookImg> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                EbookImg data = new EbookImg();
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());
                // Date today = new Date();
                // data.setUploaddate(today.toString());
                fileList.add(data);

                // 파일 저장
                File saveFile = new File(uploadFolder, saveFileName);
                try {
                    file.transferTo(saveFile);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            }
        }

        EbookVO fileboard = new EbookVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(ebook);
        eBookService.insertFileboard(fileboard);

        return "redirect:/Ebook/getEbook?no=" + postNo;
    }

}
