package com.pro06.controller;

import com.pro06.dto.TbookVO;
import com.pro06.entity.Tbook;
import com.pro06.entity.TbookImg;
import com.pro06.service.Book.TBookServiceImpl;
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
@RequestMapping("/Tbook/*")
public class TbookController {

    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private TBookServiceImpl tBookService;

    @GetMapping("TbookList")
    public String TbookList(Model model) throws Exception{
        List<Tbook> tbookList = tBookService.TbookList();

        List<TbookVO> voList = new ArrayList<>();
        for (Tbook tbook:tbookList) {
            TbookVO vo = new TbookVO();
            vo.setFileBoard(tbook);
            TbookImg tbookImg = tBookService.thmbn(tbook.getNo());
            List<TbookImg> fileList = new ArrayList<>();
            fileList.add(tbookImg);
            vo.setFileList(fileList);
            voList.add(vo);
        }
        model.addAttribute("TbookList", voList);

        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" + tbookList);
        return "Tbook/TbookList";
    }

    @GetMapping("getTbook")
    public String getFileboard(@RequestParam("no") int no, Model model) throws Exception{
        TbookVO fileboard = new TbookVO();

        Tbook tbook = tBookService.getTbook(no);
        List<TbookImg> fileList = tBookService.getFileGroupList(no);
        fileboard.setFileBoard(tbook);
        fileboard.setFileList(fileList);

        model.addAttribute("Tbook", fileboard);
        model.addAttribute("fileList", fileboard.getFileList());

        return "Tbook/getTbook";
    }

    @GetMapping("TbookInsert")
    public String TbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "Tbook/TbookInsert";
    }

    @GetMapping("fileUpload")
    public String fileUploadForm() {
        return "Tbook/TbookInsert";
    }

    @PostMapping("fileUpload")
    public String fileUpload(@RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Tbook tbook = new Tbook();
        // tbook.setNo(params.get("no"));
        tbook.setId(params.get("id"));
        tbook.setTitle(params.get("title"));
        tbook.setContent(params.get("content"));
        tbook.setServecontent(params.get("servecontent"));
        tbook.setPrice(Integer.valueOf(params.get("price")));
        tbook.setPublish(params.get("publish"));

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
        List<TbookImg> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                TbookImg data = new TbookImg();
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

        TbookVO fileboard = new TbookVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(tbook);
        tBookService.insertFileboard(fileboard);
        return "redirect:/Tbook/TbookList";
    }

    @GetMapping("TbookDelete")
    public String TbookDelete(@RequestParam("no") Integer postNo, HttpServletRequest req) throws Exception {

        //실제 파일 삭제 로직
        //파일 경로 지정

        List<TbookImg> fileList = tBookService.getFileGroupList(postNo);
        for (TbookImg tbookImg : fileList) {
            File file = new File(uploadFolder + "/" + tbookImg.getSavefile());
            if (file.exists()) { // 해당 파일이 존재하면
                file.delete(); // 파일 삭제
            }
        }
        //데이터베이스의 파일 자료실과 파일의 내용 삭제
        int ck = tBookService.removeFileboard(postNo);
        return "redirect:/Tbook/TbookList";
    }


    @GetMapping("TbookUpdate")
    public String modifyFileboard(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Tbook tbook = tBookService.getTbook(postNo);
        List<TbookImg> fileList = tBookService.getFileGroupList(postNo);
        model.addAttribute("Tbook", tbook);
        model.addAttribute("fileList", fileList);
        return "Tbook/TbookUpdate";
    }

    @PostMapping("TbookUpdate")
    public String modifyFileboard2(@RequestParam("Tbno") Integer postNo,
                                   @RequestParam("files") List<MultipartFile> files,
                                   @RequestParam Map<String, String> params,
                                   HttpServletRequest req, Model model) throws Exception {

        TbookVO fileboard = new TbookVO();

        // Create the 'board' object
        Tbook tbook = new Tbook();
        tbook.setId(params.get("id"));
        tbook.setTitle(params.get("title"));
        tbook.setContent(params.get("content"));
        tbook.setServecontent(params.get("servecontent"));
        tbook.setPrice(Integer.valueOf(params.get("price")));
        tbook.setPublish(params.get("publish"));


        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" dispatcher-servlet에서 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);
        //여러 파일 반복 저장
        List<TbookImg> fileList = new ArrayList<>();

        boolean checkFile = true;

        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {

                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                TbookImg data = new TbookImg();
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());
                Date today = new Date();
                data.setUploaddate(today.toString());
                data.setTbno(postNo);
                fileList.add(data);

                File saveFile = new File(uploadFolder, saveFileName); //실제 파일 객체 생성

                try {
                    file.transferTo(saveFile);  //실제 디렉토리에 해당파일 저장
//                file.transferTo(devFile); //개발자용 컴퓨터에 해당파일 저장
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            } else {
                checkFile = false;
                break;
            }
        }

        if(checkFile) { // 파일이 있는 경우
            List<TbookImg> fileList2 = tBookService.getFileGroupList(postNo);
            for (TbookImg tbookImg : fileList2) {
                File file = new File(uploadFolder + "/" + tbookImg.getSavefile());
                if (file.exists()) { // 해당 파일이 존재하면
                    file.delete(); // 파일 삭제
                }
            }
            tBookService.removeFileAll(postNo);
            fileboard.setFileList(fileList); // 파일
            fileboard.setFileBoard(tbook); //글 제목 내용
            tBookService.updateFileboard(fileboard); // 모든 내용 업데이트
        } else { // 파일이 없는 경우
            tBookService.updateTbook(tbook); // 글 제목 내용만 업데이트
        }

        return "redirect:/Tbook/getTbook?no=" + postNo;
    }

}
