package com.pro06.controller;


import com.pro06.dto.HbookVO;
import com.pro06.dto.MbookVO;
import com.pro06.entity.Hbook;
import com.pro06.entity.HbookImg;
import com.pro06.entity.Mbook;
import com.pro06.entity.MbookImg;
import com.pro06.service.Book.HBookServiceImpl;
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
@RequestMapping("/Hbook/*")
public class HbookController {

    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private HBookServiceImpl hBookService;

    @GetMapping("HbookList")
    public String HbookList(Model model) throws Exception{
        List<Hbook> hbookList = hBookService.HbookList();

        List<HbookVO> voList = new ArrayList<>();
        for (Hbook hbook:hbookList) {
            HbookVO vo = new HbookVO();
            vo.setFileBoard(hbook);
            HbookImg hbookImg = hBookService.thmbn(hbook.getNo());
            List<HbookImg> fileList = new ArrayList<>();
            fileList.add(hbookImg);
            vo.setFileList(fileList);
            voList.add(vo);
        }
        model.addAttribute("HbookList", voList);

        log.info(" ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" + hbookList);
        return "Hbook/HbookList";
    }

    @GetMapping("getHbook")
    public String getFileboard(@RequestParam("no") int no, Model model) throws Exception{
        HbookVO fileboard = new HbookVO();

        Hbook hbook = hBookService.getHbook(no);
        List<HbookImg> fileList = hBookService.getFileGroupList(no);
        fileboard.setFileBoard(hbook);
        fileboard.setFileList(fileList);

        model.addAttribute("Hbook", fileboard);
        model.addAttribute("fileList", fileboard.getFileList());

        return "Hbook/getHbook";
    }

    @GetMapping("HbookInsert")
    public String HbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "Hbook/HbookInsert";
    }

    @GetMapping("fileUpload")
    public String fileUploadForm() {
        return "Hbook/HbookInsert";
    }

    @PostMapping("fileUpload")
    public String fileUpload(@RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Hbook hbook = new Hbook();
        // hbook.setNo(params.get("no"));
        hbook.setId(params.get("id"));
        hbook.setTitle(params.get("title"));
        hbook.setContent(params.get("content"));
        hbook.setServecontent(params.get("servecontent"));
        hbook.setPrice(Integer.valueOf(params.get("price")));
        hbook.setPublish(params.get("publish"));

/*
        // 가격에 대한 String 값을 BigDecimal로 변환
        String priceString = params.get("price");
        BigDecimal price = new BigDecimal(priceString);
        hbook.setPrice(price);

        // 출간일에 대한 String 값을 LocalDate로 변환
        String publishString = params.get("publish");
        LocalDate publish = LocalDate.parse(publishString);
        hbook.setPublish(publish);
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
        List<HbookImg> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                HbookImg data = new HbookImg();
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

        HbookVO fileboard = new HbookVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(hbook);
        hBookService.insertFileboard(fileboard);
        return "redirect:/Hbook/HbookList";
    }

    @GetMapping("HbookDelete")
    public String HbookDelete(@RequestParam("no") Integer postNo, HttpServletRequest req) throws Exception {

        //실제 파일 삭제 로직
        //파일 경로 지정

        List<HbookImg> fileList = hBookService.getFileGroupList(postNo);
        for (HbookImg hbookImg : fileList) {
            File file = new File(uploadFolder + "/" + hbookImg.getSavefile());
            if (file.exists()) { // 해당 파일이 존재하면
                file.delete(); // 파일 삭제
            }
        }
        //데이터베이스의 파일 자료실과 파일의 내용 삭제
        int ck = hBookService.removeFileboard(postNo);
        return "redirect:/Hbook/HbookList";
    }

    // 거래글 수정폼 이동
    @GetMapping("HbookUpdate")
    public String modifyFileboard(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Hbook hbook = hBookService.getHbook(postNo);
        List<HbookImg> fileList = hBookService.getFileGroupList(postNo);
        model.addAttribute("Hbook", hbook);
        model.addAttribute("fileList", fileList);
        return "Hbook/HbookUpdate";
    }

    @PostMapping("HbookUpdate")
    public String fileUpload(@RequestParam("Hbno") Integer postNo,
                             @RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Hbook hbook = new Hbook();
        // hbook.setNo(params.get("no"));
        hbook.setId(params.get("id"));
        hbook.setTitle(params.get("title"));
        hbook.setContent(params.get("content"));
        hbook.setServecontent(params.get("servecontent"));
        hbook.setPrice(Integer.valueOf(params.get("price")));
        hbook.setPublish(params.get("publish"));

/*
        // 가격에 대한 String 값을 BigDecimal로 변환
        String priceString = params.get("price");
        BigDecimal price = new BigDecimal(priceString);
        hbook.setPrice(price);

        // 출간일에 대한 String 값을 LocalDate로 변환
        String publishString = params.get("publish");
        LocalDate publish = LocalDate.parse(publishString);
        hbook.setPublish(publish);
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
        List<HbookImg> fileList = new ArrayList<>();
        // 파일 리스트를 순회하며 각 파일 처리
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // ... (기존 파일 처리 로직)
                HbookImg data = new HbookImg();
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

        HbookVO fileboard = new HbookVO();
        fileboard.setFileList(fileList);
        fileboard.setFileBoard(hbook);
        hBookService.insertFileboard(fileboard);

        return "redirect:/Hbook/getHbook?no=" + postNo;
    }

}
