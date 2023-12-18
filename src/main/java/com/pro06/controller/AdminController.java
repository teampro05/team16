package com.pro06.controller;

import com.pro06.dto.course.*;
import com.pro06.entity.*;
import com.pro06.service.UserService;
import com.pro06.service.course.CourseServiceImpl;
import com.pro06.service.course.LectureServiceImpl;
import com.pro06.service.course.VideoServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@Controller
@RequestMapping("/admin/*")
public class AdminController {

    // 실제 업로드 디렉토리
    // thymeleaf 에서는 외부에 지정하여 사용해야 한다.
    // jsp와는 다르게 webapp이 없기 때문이다.
    // resources는 정적이라 업데이트 되어도 파일을 못 찾기에 서버를 재 시작 해야함
    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private LectureServiceImpl lectureService;

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private UserService userService;

    @Autowired
    private VideoServiceImpl videoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/home")
    public String Home(Model model) {
        return "/admin/home";
    }

    // 강좌 목록
    @GetMapping("courseList")
    public String courseList(Model model) {
        List<CourseDto> courseList = courseService.admCourseList();
        model.addAttribute("courseList", courseList);
        return "admin/course/courseList";
    }

    // 강좌 상세
    @GetMapping("courseDetail")
    public String courseDetail(@RequestParam("no") Integer no, Model model) throws IOException {

        // 강좌 상세
        CourseDto course = courseService.getCourse(no);
        model.addAttribute("course", course);

        // 강의 목록
        List<LectureDto> lectureList = lectureService.lectureCnoList(no);
        model.addAttribute("lectureList", lectureList);
        return "admin/course/courseDetail";
    }

    // 강좌 생성폼 이동
    @GetMapping("courseInsert")
    public String courseInsert(Principal principal, Model model) {
        String id = principal.getName();
        model.addAttribute("id", id);
        return "admin/course/courseInsert";
    }

    // 강좌 생성
    @PostMapping("courseInsert")
    public String courseInsert(CourseDto courseDto, Model model) {
        courseService.courseInsert(courseDto);
        return "redirect:/admin/courseList";
    }

    // 강좌 수정폼 이동
    @GetMapping("courseUpdate")
    public String courseUpdate(Principal principal, @RequestParam("no") Integer no, Model model) {
        String id = principal.getName();
        CourseDto courseDto = courseService.getCourse(no);
        model.addAttribute("dto", courseDto);
        model.addAttribute("id", id);
        return "admin/course/courseUpdate";
    }

    // 강좌 생성
    @PostMapping("courseUpdate")
    public String courseUpdate(CourseDto courseDto) {
        courseService.courseUpdate(courseDto);
        return "redirect:/admin/courseList";
    }

    // 강좌 삭제
    @GetMapping("courseDelete")
    public String courseDelete(@RequestParam("no") Integer no) {
        courseService.courseDelete(no);
        return "redirect:/admin/courseList";
    }


    // lecture
    // 강의 생성폼 이동
    @GetMapping("lectureInsert")
    public String lectureInsert(Principal principal, @RequestParam("cno") Integer cno, Model model) {
        String id = principal.getName();

        model.addAttribute("id", id);
        model.addAttribute("cno", cno);
        return "admin/lecture/lectureInsert";
    }

    // spring boot 3 이상부터 이런식으로 사용해야 함
    // 강의 생성
    // 강의 정보, 강좌 번호, 파일 추출
    @PostMapping("lectureInsert")
    public String lectureInsertPro(MultipartHttpServletRequest req) throws Exception {

        // 입력된 파일목록
        List<MultipartFile> files = new ArrayList<>();
        // 강의 정보
        LectureDto lecture = new LectureDto();
        // 여러 파일 반복 저장
        List<VideoDto> fileList = new ArrayList<>();

        // 파일 저장
        for (int i = 0; i < req.getFiles("files").size(); i++) {
            files.add(req.getFiles("files").get(i));
        }

        // 강의정보 저장
        CourseDto course = new CourseDto();
        Integer cno = Integer.parseInt(req.getParameter("cno"));
        course.setNo(cno);
        lecture.setCourse(course);      // cno 저장
        lecture.setId(req.getParameter("id"));
        lecture.setTitle(req.getParameter("title"));
        lecture.setContent(req.getParameter("content"));
        lecture.setKeyword(req.getParameter("keyword"));

        // 시험정보 저장
        LecTestDto lecTest = new LecTestDto();
        lecTest.setExam1(req.getParameter("exam1"));
        lecTest.setExam2(req.getParameter("exam2"));
        lecTest.setExam3(req.getParameter("exam3"));
        lecTest.setExam4(req.getParameter("exam4"));
        lecTest.setExam5(req.getParameter("exam5"));
        lecTest.setAnswer1(req.getParameter("answer1"));
        lecTest.setAnswer2(req.getParameter("answer2"));
        lecTest.setAnswer3(req.getParameter("answer3"));
        lecTest.setAnswer4(req.getParameter("answer4"));
        lecTest.setAnswer5(req.getParameter("answer5"));

        // 파일 추출 테스트
        for (int i = 0; i < req.getFiles("files").size(); i++) {
            log.info("req.getParameter(\"title\") : " + req.getParameter("title"));
            log.info("req.getFile(\"files\") : " + req.getFile("files"));
            log.info("req.getFile(\"files\").getOriginalFilename() : " +
                    req.getFiles("files").get(i).getOriginalFilename());      // 실제 파일이름 출력
            log.info("req.getFiles(\"files\").get(" + i + ").getBytes() : " +
                    req.getFiles("files").get(i).getBytes());                 // 파일의 용량 출력
            log.info("req.getFiles(\"files\").get(" + i + ").getName() : " +
                    req.getFiles("files").get(i).getName());                  // name 속성값 출력
            log.info("req.getFiles(\"files\").size() : " +
                    req.getFiles("files").size());                            // 입력된 파일의 개수 출력
        }

        // 만약 저장 폴더가 없다면 생성
        File folder = new File(uploadFolder);
        if (!folder.exists()) folder.mkdirs();

        // log 출력
        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 파일 저장 경로 : " + uploadFolder);

        // 첨부된 파일(MultipartFile) 처리
        if (files != null && files.size() > 0) {
            for (MultipartFile file : files) {
                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성

                // 저장위치, 실제파일이름, 저장될 파일이름, 파일크기 정보를 저장
                VideoDto data = new VideoDto();
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());
                fileList.add(data);

                // 파일 저장
                File saveFile = new File(uploadFolder, saveFileName);
                try {
                    file.transferTo(saveFile); // 실제 upload 위치에 파일 저장
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            }
        }
        // VO를 통해 db에 저장
        LectureVO lectureVO = new LectureVO();
        lectureVO.setLecture(lecture);
        lectureVO.setFileList(fileList);
        lectureVO.setLecTest(lecTest);
        lectureVO.setCno(cno);
        lectureService.LectureVoInsert(lectureVO); // 강의와 비디오를 같이 저장

        return "redirect:/admin/courseDetail?no=" + cno;
    }

    // 질문 목록
    @GetMapping("lecQueList")
    public String lecQueList(Model model) {
        List<LecQueDto> dtoList = lectureService.lecQueDtoFindByAll();
        model.addAttribute("dtoList", dtoList);
        return "admin/lecture/lecQueList";
    }

    // 질문의 답변 상세 폼 이동
    @GetMapping("ansInsUpd")
    public String ansInsUpd(@RequestParam("no") Integer no, Model model) {
        LecQueDto dto = lectureService.getLecQue(no);

        // 영상의 파일 정보 추출
        List<VideoDto> videoList = videoService.videoList(dto.getCourse().getNo(), dto.getLecture().getNo());
        Integer page = dto.getPage();
        String savefile = videoList.get(page).getSavefile();

        model.addAttribute("savefile", savefile);
        model.addAttribute("dto", dto);
        return "admin/lecture/ansInsUpd";
    }
    
    // 질문의 답변 작성, 수정
    @PostMapping("ansInsUpd")
    public String ansInsUpdPro(LecQueDto lecQueDto) {
        lectureService.lecQueAnsInsUpd(lecQueDto);
        return "redirect:/admin/lecQueList";
    }

    // 질문의 답변 삭제
    @GetMapping("ansDelete")
    public String ansDelete(LecQueDto lecQueDto) {
        lectureService.lecQueDelete(lecQueDto.getNo());
        return "redirect:/admin/lecQueList";
    }

    // 회원 관리
    @GetMapping("userList")
    public String memberList(Model model){
        List<User> userList = userService.userList();
        model.addAttribute("userList", userList);
        return "/admin/userList";
    }

    @GetMapping("/userGet")
    public String userGet(String id, Model model){
        User user = userService.getId(id);
        model.addAttribute("user", user);
        return "/admin/userGet";
    }

    @PostMapping("/changeStatus_list")
    public String out1(String id, Model model, Status status){
        User user = userService.getId(id);
        user.setStatus(status);
        userService.userUpdate(user);
        return "redirect:/admin/userList";
    }

    @PostMapping("/changeStatus_get")
    public String out2(String id, Model model, Status status){
        User user = userService.getId(id);
        user.setStatus(status);
        userService.userUpdate(user);
        return "redirect:/admin/userGet?id="+id;
    }
}