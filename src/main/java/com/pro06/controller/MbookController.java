package com.pro06.controller;


import com.pro06.dto.MbookVO;
import com.pro06.entity.Mbook;
import com.pro06.entity.MbookImg;
import com.pro06.service.Book.MBookServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
@Slf4j
//@CrossOrigin("http://localhost:8085")
@RequestMapping("/Mbook/*")
public class MbookController {

    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private MBookServiceImpl mBookService;

    @GetMapping("MbookList")
    public String MbookList(Model model) throws Exception{
        List<Mbook> mbookList = mBookService.MbookList();

        List<MbookVO> voList = new ArrayList<>();
        for (Mbook mbook:mbookList) {
            MbookVO vo = new MbookVO();
            vo.setFileBoard(mbook);
            MbookImg mbookImg = mBookService.thmbn(mbook.getNo());
            List<MbookImg> fileList = new ArrayList<>();
            fileList.add(mbookImg);
            vo.setFileList(fileList);
            voList.add(vo);
        }
        model.addAttribute("MbookList", voList);

        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" + mbookList);
        return "Mbook/MbookList";
    }

    @GetMapping("getMbook")
    public String getFileboard(@RequestParam("no") int no, Model model) throws Exception{
        MbookVO fileboard = new MbookVO();

        Mbook mbook = mBookService.getMbook(no);
        List<MbookImg> fileList = mBookService.getFileGroupList(no);
        fileboard.setFileBoard(mbook);
        fileboard.setFileList(fileList);

        model.addAttribute("Mbook", fileboard);
        model.addAttribute("fileList", fileboard.getFileList());

        return "Mbook/getMbook";
    }

    @GetMapping("MbookInsert")
    public String MbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "Mbook/MbookInsert";
    }

    @GetMapping("fileUpload")
    public String fileUploadForm() {
        return "Mbook/MbookInsert";
    }

    @PostMapping("fileUpload")
    public String fileUpload(@RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Mbook mbook = new Mbook();
        // mbook.setNo(params.get("no"));
        mbook.setId(params.get("id"));
        mbook.setTitle(params.get("title"));
        mbook.setContent(params.get("content"));
        mbook.setServecontent(params.get("servecontent"));
        mbook.setPrice(Integer.valueOf(params.get("price")));
        mbook.setPublish(params.get("publish"));

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
        List<MbookImg> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                MbookImg data = new MbookImg();
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

        MbookVO fileboard = new MbookVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(mbook);
        mBookService.insertFileboard(fileboard);
        return "redirect:/Mbook/MbookList";
    }

    @GetMapping("MbookDelete")
    public String MbookDelete(@RequestParam("no") Integer postNo, HttpServletRequest req) throws Exception {

        //실제 파일 삭제 로직
        //파일 경로 지정

        List<MbookImg> fileList = mBookService.getFileGroupList(postNo);
        for (MbookImg mbookImg : fileList) {
            File file = new File(uploadFolder + "/" + mbookImg.getSavefile());
            if (file.exists()) { // 해당 파일이 존재하면
                file.delete(); // 파일 삭제
            }
        }
        //데이터베이스의 파일 자료실과 파일의 내용 삭제
        int ck = mBookService.removeFileboard(postNo);
        return "redirect:/Mbook/MbookList";
    }

    // 거래글 수정폼 이동
    @GetMapping("MbookUpdate")
    public String modifyFileboard(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Mbook mbook = mBookService.getMbook(postNo);
        List<MbookImg> fileList = mBookService.getFileGroupList(postNo);
        model.addAttribute("Mbook", mbook);
        model.addAttribute("fileList", fileList);
        return "Mbook/MbookUpdate";
    }

    @PostMapping("MbookUpdate")
    public String fileUpload(@RequestParam("Mbno") Integer postNo,
                             @RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Mbook mbook = new Mbook();
        // mbook.setNo(params.get("no"));
        mbook.setId(params.get("id"));
        mbook.setTitle(params.get("title"));
        mbook.setContent(params.get("content"));
        mbook.setServecontent(params.get("servecontent"));
        mbook.setPrice(Integer.valueOf(params.get("price")));
        mbook.setPublish(params.get("publish"));

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
        List<MbookImg> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                MbookImg data = new MbookImg();
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

        MbookVO fileboard = new MbookVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(mbook);
        mBookService.insertFileboard(fileboard);

        return "redirect:/Mbook/getMbook?no=" + postNo;
    }

}
