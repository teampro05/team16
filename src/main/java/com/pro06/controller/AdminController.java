package com.pro06.controller;

import com.pro06.dto.*;
import com.pro06.dto.course.*;
import com.pro06.entity.*;
import com.pro06.service.BoardService;
import com.pro06.service.Book.EBookServiceImpl;
import com.pro06.service.Book.HBookServiceImpl;
import com.pro06.service.Book.MBookServiceImpl;
import com.pro06.service.Book.TBookServiceImpl;
import com.pro06.service.UserService;
import com.pro06.service.course.CourseServiceImpl;
import com.pro06.service.course.LectureServiceImpl;
import com.pro06.service.course.VideoServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Log4j2
@Component
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

    @Autowired
    private BoardService boardService;

    @GetMapping("/home")
    public String Home(Model model) {
        return "redirect:/admin/userList";
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
        
        // ot 영상 추출
        CourseVideoDto cvd = videoService.getCourseVideo(no);
        model.addAttribute("cvd", cvd);

        // 강의 목록
        List<LectureDto> lectureList = lectureService.admlectureList(no);
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
    public String courseInsertPro(MultipartHttpServletRequest req) {

        // 입력된 파일
        MultipartFile file = req.getFile("file");
        // 저장된 파일 정보 log 출력
        log.warn("file.toString() : " + file.toString());

        // 강좌 정보 저장
        CourseDto courseDto = new CourseDto();
        courseDto.setId(req.getParameter("id"));
        courseDto.setTitle(req.getParameter("title"));
        courseDto.setContent(req.getParameter("content"));
        courseDto.setLevel(req.getParameter("level"));
        courseDto.setPeo_max(Integer.parseInt(req.getParameter("peo_max")));
        courseDto.setCopen(Integer.parseInt(req.getParameter("copen")));
        courseDto.setCopendate(LocalDateTime.parse(req.getParameter("copendate")));
        CourseDto resDto = courseService.courseInsert(courseDto);

        // 파일 저장
        // 저장위치, 실제파일이름, 저장될 파일이름, 파일크기 정보를 저장
        CourseVideoDto data = new CourseVideoDto();
        if (!file.isEmpty()) {
            // 파일 처리 로직 시작
            String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
            String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
            String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
            String saveFileName = "ot_" + randomUUID + Extension;  // 저장할 파일 이름 생성

            data.setSavefolder(uploadFolder);
            data.setOriginfile(file.getOriginalFilename());
            data.setSavefile(saveFileName);
            data.setFilesize(file.getSize());
            data.setCourse(resDto);

            // 파일 저장
            File saveFile = new File(uploadFolder, saveFileName);
            try {
                file.transferTo(saveFile); // 실제 upload 위치에 파일 저장
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
            videoService.couVdoInsert(data);
        }
        
        return "redirect:/admin/courseList";
    }

    // 강좌 수정폼 이동
    @GetMapping("courseUpdate")
    public String courseUpdate(Principal principal, @RequestParam("no") Integer no, Model model) {
        String id = principal.getName();
        CourseDto courseDto = courseService.getCourse(no);
        CourseVideoDto cvd = videoService.getCourseVideo(no);
        model.addAttribute("dto", courseDto);
        model.addAttribute("id", id);
        model.addAttribute("cvd", cvd);

        return "admin/course/courseUpdate";
    }

    // 강좌 생성
    @PostMapping("courseUpdate")
    public String courseUpdate(MultipartHttpServletRequest req, Model model) {

        // 입력된 파일
        MultipartFile file = req.getFile("file");
        // 저장된 파일 정보 log 출력
        log.warn("file.toString() : " + file.toString());

        Integer no = Integer.parseInt(req.getParameter("no"));

        // 강좌 정보 저장
        CourseDto courseDto = new CourseDto();
        courseDto.setNo(no);
        courseDto.setId(req.getParameter("id"));
        courseDto.setTitle(req.getParameter("title"));
        courseDto.setContent(req.getParameter("content"));
        courseDto.setLevel(req.getParameter("level"));
        courseDto.setPeo_max(Integer.parseInt(req.getParameter("peo_max")));
        courseDto.setCopen(Integer.parseInt(req.getParameter("copen")));
        courseDto.setCopendate(LocalDateTime.parse(req.getParameter("copendate")));
        courseService.courseUpdate(courseDto);

        // 저장위치, 실제파일이름, 저장될 파일이름, 파일크기 정보를 저장
        CourseVideoDto data = new CourseVideoDto();

        if(!file.isEmpty()) {      // 새로운 파일이 들어온 경우, 파일이 있는 경우
            log.warn("!file.isEmpty() : " + !file.isEmpty());
            // 파일 처리 로직 시작
            String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
            String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
            String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
            String saveFileName = "ot_" + randomUUID + Extension;  // 저장할 파일 이름 생성

            data.setSavefolder(uploadFolder);
            data.setOriginfile(file.getOriginalFilename());
            data.setSavefile(saveFileName);
            data.setFilesize(file.getSize());
            data.setCourse(courseDto);

            // 파일 저장
            File saveFile = new File(uploadFolder, saveFileName);
            
            // 기존 파일 이름 가져오기
            CourseVideoDto videoDto = videoService.getCourseVideo(no);
            videoService.couVdoUpdate(data);
            try {
                file.transferTo(saveFile); // 실제 upload 위치에 파일 저장
                File remove = new File(uploadFolder + "/" + videoDto.getSavefile());
                if (remove.exists()) { // 해당 파일이 존재하면
                    remove.delete(); // 파일 삭제
                }
            } catch (IllegalStateException | IOException e) {
                e.printStackTrace();
                // 예외 처리
            }
        }
        return "redirect:/admin/courseList";
    }

    // 강좌 삭제, 복구
    @PostMapping("couDelRec")
    public String couDelRec(CourseDto courseDto) {
        courseService.couDelRec(courseDto);
        return "redirect:/admin/courseList";
    }


    // lecture
    // 강의 생성 폼 이동
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
        for (int i = 1; i <= 5; i++) {
            files.add(req.getFile("file" + i));
        }

        // 파일 추출 데이터 확인
        for (int i = 0; i < files.size(); i++) {
            log.warn("files.get(" + i + ").toString() : " + files.get(i).toString());             // 입력된 파일의 개수 출력
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

    
    // 강의 수정 폼 이동
    @GetMapping("lectureUpdate")
    public String lectureUpdate(@RequestParam("lno") Integer lno,
                                @RequestParam("cno") Integer cno,
                                Principal principal, Model model) throws Exception {

        if(principal.getName() != null) {
            LectureVO vo = lectureService.getLectureVo(cno, lno);
            LectureDto lecDto = vo.getLecture();
            List<VideoDto> videoDtoList = vo.getFileList();
            LecTestDto testDto = vo.getLecTest();

            List<String> examList = new ArrayList<>();
            examList.add(testDto.getExam1());
            examList.add(testDto.getExam2());
            examList.add(testDto.getExam3());
            examList.add(testDto.getExam4());
            examList.add(testDto.getExam5());

            List<String> ansList = new ArrayList<>();
            ansList.add(testDto.getAnswer1());
            ansList.add(testDto.getAnswer2());
            ansList.add(testDto.getAnswer3());
            ansList.add(testDto.getAnswer4());
            ansList.add(testDto.getAnswer5());

            model.addAttribute("lecDto", lecDto);
            model.addAttribute("videoDtoList", videoDtoList);
            model.addAttribute("testDto", testDto);
            model.addAttribute("examList", examList);
            model.addAttribute("ansList", ansList);
            model.addAttribute("id", principal.getName());
            return "admin/lecture/lectureUpdate";
        } else {
            return "redirect:/";
        }
    }

    // spring boot 3 이상부터 이런식으로 사용해야 함
    // 강의 수정
    // 강의 정보, 강좌 번호, 파일 추출
    @PostMapping("lectureUpdate")
    public String lectureUpdatePro(MultipartHttpServletRequest req) throws Exception {

        Integer cno = Integer.parseInt(req.getParameter("cno"));
        Integer lno = Integer.parseInt(req.getParameter("lno"));
        Integer tno = Integer.parseInt(req.getParameter("tno"));    // lecTest no

        // 입력된 파일목록
        List<MultipartFile> files = new ArrayList<>();
        // 강의 정보
        LectureDto lecture = new LectureDto();
        // 여러 파일 반복 저장
        List<VideoDto> fileList = new ArrayList<>();

        // 파일 저장
        for (int i = 1; i <= 5; i++) {
            files.add(req.getFile("file" + i));
        }

        // 파일 추출 데이터 확인
        for (int i = 0; i < files.size(); i++) {
            log.warn("files.get(" + i + ").toString() : " + files.get(0).toString());             // 입력된 파일의 개수 출력
        }

        // 강의정보 저장
        CourseDto course = new CourseDto();
        course.setNo(cno);
        lecture.setNo(lno);
        lecture.setCourse(course);      // cno 저장
        lecture.setId(req.getParameter("id"));
        lecture.setTitle(req.getParameter("title"));
        lecture.setContent(req.getParameter("content"));
        lecture.setKeyword(req.getParameter("keyword"));

        // 시험정보 저장
        LecTestDto lecTest = new LecTestDto();
        lecTest.setNo(tno);
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

        // 만약 저장 폴더가 없다면 생성
        File folder = new File(uploadFolder);
        if (!folder.exists()) folder.mkdirs();

        // log 출력
        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 파일 저장 경로 : " + uploadFolder);

        for (MultipartFile file : files) {
            if(!file.isEmpty()) {      // 새로운 파일이 들어온 경우, 파일이 있는 경우
                log.warn("!file.isEmpty() : " + !file.isEmpty());
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
            } else {    // 새로운 파일이 없는 경우 null 값 객체 주입
                VideoDto data = new VideoDto();
                fileList.add(data);
            }
        }

        // VO를 통해 db에 저장
        LectureVO lectureVO = new LectureVO();
        lectureVO.setLecture(lecture);
        lectureVO.setFileList(fileList);
        lectureVO.setLecTest(lecTest);
        lectureVO.setCno(cno);
        lectureService.LectureVoUpdate(lectureVO); // 강의와 비디오, 시험을 같이 저장

        return "redirect:/admin/courseDetail?no=" + cno;
    }

    // 강의 삭제, 복구
    // 강좌 삭제, 복구
    @PostMapping("lecDelRec")
    public String lecDelRec(@RequestParam("cno") Integer cno, LectureDto dto) {
        lectureService.lecDelRec(dto);
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
/*    @GetMapping("ansDelete")
    public String ansDelete(LecQueDto lecQueDto) {
        lectureService.lecQueDelete(lecQueDto.getNo());
        return "redirect:/admin/lecQueList";
    }*/

    // 질문의 답변 삭제, 복구
    @PostMapping("ansDelRec")
    public String ansDelRec(LecQueDto lecQueDto) {
        lectureService.lecQueDelete(lecQueDto);
        return "redirect:/admin/lecQueList";
    }

    // 질문의 답변 복구
    @GetMapping("ansRecover")
    public String ansRecover(LecQueDto lecQueDto) {
        lectureService.lecQueAnsRecover(lecQueDto.getNo());
        return "redirect:/admin/lecQueList";
    }

    // 회원 관리

    @GetMapping("userList")
    public String memberList(Model model){
        List<UserDTO> userList = userService.userList();
        model.addAttribute("userList", userList);
        return "/admin/userList";
    }

    @GetMapping("/userGet")
    public String userGet(String id, Model model){
        UserDTO userDTO = userService.getId(id);
        model.addAttribute("user", userDTO);
        return "/admin/userGet";
    }

    @PostMapping("/changeStatus_list")
    public String out1(String id, Model model, Status status){
        UserDTO userDTO = userService.getId(id);
        userDTO.setStatus(status);
        userService.userUpdate(userDTO);
        return "redirect:/admin/userList";
    }

    @PostMapping("/changeStatus_get")
    public String out2(String id, Model model, Status status){
        UserDTO userDTO = userService.getId(id);
        userDTO.setStatus(status);
        userService.userUpdate(userDTO);
        return "redirect:/admin/userGet?id="+id;
    }
    @PostMapping("/role_list")
    public String role1(String id, Model model, Role role){
        UserDTO userDTO = userService.getId(id);
        userDTO.setRole(role);
        userService.userUpdate(userDTO);
        return "redirect:/admin/userList";
    }

    @PostMapping("/role_get")
    public String role2(String id, Model model, Role role){
        UserDTO userDTO = userService.getId(id);
        userDTO.setRole(role);
        userService.userUpdate(userDTO);
        return "redirect:/admin/userGet?id="+id;
    }



    // 자동으로 개강진행
    @Scheduled(fixedRate = 20000)
    @GetMapping("/openCource")
    public void courseOpen () {
        log.info("openCource ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ");
        List<CourseDto> courseList = courseService.admCourseList();
        LocalDateTime date = LocalDateTime.now();
        //현재 시간 String으로 변경
        String Local1 = date.format(DateTimeFormatter.BASIC_ISO_DATE);

        for (CourseDto courseDto:courseList){
            //강의 시간 String으로 변경
            if(courseDto.getCopen().equals(0)){
                String Local2 = courseDto.getCopendate().format(DateTimeFormatter.BASIC_ISO_DATE);
                if(Local1.equals(Local2)) {
                    //값이 같을 경우 강의 오픈
                    courseDto.setCopen(1);
                    courseService.courseUpdate(courseDto);
                    log.info("오픈된 강의 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" + courseDto.getTitle());
                }
            };
        }
    }

    @PostMapping("/openCource_admin")
    public String out1(Integer no, Model model, Integer copen ){
        CourseDto courseDto = courseService.getCourse(no);
        courseDto.setCopen(copen);
        courseService.courseUpdate(courseDto);
        return "redirect:/admin/courseList";
    }



    // Notice

    @GetMapping("/notice_Admin")
    public String notice(Model model) {
        List<BoardDTO> noticeList = boardService.NoticeList();
        model.addAttribute("noticeList", noticeList);
        return "/admin/board/noticeList";
    }

    @GetMapping("/noticeGet_Admin")
    public String noticeGet(Model model, Integer no) {
        BoardDTO boardDTO = boardService.NoticeGet(no);
        model.addAttribute("notice", boardDTO);
        return "/admin/board/noticeGet";
    }


    @GetMapping("/noticeAdd_Admin")
    public String noticeForm(Model model, Principal principal) {
        model.addAttribute("boardDTO", new BoardDTO());
        model.addAttribute("prin", principal.getName());
        return "/admin/board/noticeAdd";
    }

    @PostMapping("/noticeAdd_Admin")
    public String noticeInsert(BoardDTO boardDTO){
        boardService.NoticeInsert(boardDTO);
        return "redirect:/admin/notice_Admin";
    }

    @GetMapping("/noticeEdit_Admin")
    public String noticeEditForm(Model model, Integer no) {
        BoardDTO notice = boardService.NoticeGet(no);
        model.addAttribute("notice", notice);
        return "/admin/board/noticeEdit";
    }

    @PostMapping("noticeEdit_Admin")
    public String noticeEdit(BoardDTO boardDTO){
        Integer no = boardDTO.getNo();
        boardService.NoticeUpdate(boardDTO);
        return "redirect:/admin/noticeGet_Admin?no="+no;
    }

    @GetMapping("/noticeDelete_Admin")
    public String noticeDelete(Model model, Integer no) {
        boardService.NoticeDelete(no);
        return "redirect:/admin/notice_Admin";
    }


    // Faq

    @GetMapping("/faqList_Admin")
    public String Faq(Model model) {
        List<BoardDTO> faqList = boardService.faqList();
        model.addAttribute("faqList", faqList);
        return "/admin/board/faqList";
    }

    @GetMapping("/faqAdd_Admin")
    public String FaqForm(Model model) {
        model.addAttribute("boarddto", new BoardDTO());
        return "/admin/board/faqAdd";
    }

    @PostMapping("/faqAdd_Admin")
    public String FaqInsert(BoardDTO boardDTO, Model model){
        boardService.faqInsert(boardDTO);
        model.addAttribute("url", 1);
        return "/alert";
    }

    @GetMapping("/faqEdit_Admin")
    public String FaqEditForm(Model model, Integer no) {
        BoardDTO faq = boardService.faqGet(no);
        model.addAttribute("faq", faq);
        return "/admin/board/faqEdit";
    }

    @PostMapping("/faqEdit_Admin")
    public String FaqEdit(BoardDTO boardDTO, Model model){
        boardService.faqUpdate(boardDTO);
        model.addAttribute("url", 1);
        return "/alert";
    }

    @GetMapping("/faqDel_Admin")
    public String FaqDel(Model model, Integer no) {
        boardService.faqDelete(no);
        return "redirect:/admin/faqList_Admin";
    }

    @ResponseBody
    @PostMapping(value = "/post", consumes = MediaType.APPLICATION_JSON_VALUE )
    public String PostTest(@RequestBody String msg) {
        return "post success!!!" + msg;
    }

    // 초등 교재

    @Autowired
    private EBookServiceImpl eBookService;

    @GetMapping("EbookList")
    public String EbookList(Model model) throws Exception{
        List<Ebook> ebookList = eBookService.admEbookList();

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

        return "admin/Ebook/EbookList";
    }

    @GetMapping("EbookInsert")
    public String EbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "admin/Ebook/EbookInsert";
    }

    @GetMapping("Ebook/fileUpload")
    public String fileUploadForm() {
        return "admin/Ebook/EbookInsert";
    }

    @PostMapping("Ebook/fileUpload")
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
                    
                    // 썸네일 저장 위치경로 + 이름
                    // 썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
                    File thumbsname= new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);
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

        return "redirect:/admin/EbookList";
    }

    @PostMapping("EbookDelRec")  // 삭제, 복구
    public String EbookDelRec(Ebook ebook) {
        eBookService.ebookDelRec(ebook);
        return "redirect:/admin/EbookList";
    }

/*
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
        return "redirect:/admin/EbookList";
    }
*/

    // 수정 폼 이동
    @GetMapping("EbookUpdate")
    public String modifyFileboard(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Ebook ebook = eBookService.getEbook(postNo);
        List<EbookImg> fileList = eBookService.getFileGroupList(postNo);
        model.addAttribute("Ebook", ebook);
        model.addAttribute("fileList", fileList);
        return "admin/Ebook/EbookUpdate";
    }

    @PostMapping("EbookUpdate")
    public String modifyFileboard2(@RequestParam("Ebno") Integer postNo,
                                   @RequestParam("files") List<MultipartFile> files,
                                   @RequestParam Map<String, String> params,
                                   HttpServletRequest req, Model model) throws Exception {
        EbookVO fileboard = new EbookVO();


        // Create the 'board' object
        Ebook ebook = new Ebook();
        ebook.setNo(postNo);
        ebook.setId(params.get("id"));
        ebook.setTitle(params.get("title"));
        ebook.setContent(params.get("content"));
        ebook.setServecontent(params.get("servecontent"));
        ebook.setPrice(Integer.valueOf(params.get("price")));
        ebook.setPublish(params.get("publish"));


        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" dispatcher-servlet에서 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);
        log.info(" ebook : " + ebook);


        //여러 파일 반복 저장
        List<EbookImg> fileList = new ArrayList<>();

        boolean checkFile = true;

        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                log.info(" file : " + file);

                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성


                EbookImg data = new EbookImg();

                data.setNo(postNo);
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());

                Date today = new Date();
                data.setUploaddate(today.toString());
                data.setEbno(postNo);
                fileList.add(data);
                File saveFile = new File(uploadFolder, saveFileName); //실제 파일 객체 생성
                log.info(" fileList : " + fileList);

                try {
                    file.transferTo(saveFile);  //실제 디렉토리에 해당파일 저장
//                file.transferTo(devFile); //개발자용 컴퓨터에 해당파일 저장
                    // 썸네일 저장 위치경로 + 이름
                    //썸네일 생성 (복사할파일, 썸네일파일, 가로, 세로)
                    File thumbsname = new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            } else {
                checkFile = false;
//                break;
            }
        }
        log.info(" checkFile1 : " + checkFile);
        if(checkFile == true) { // 파일이 있는 경우
            List<EbookImg> fileList2 = eBookService.getFileGroupList(postNo);
            for (EbookImg ebookImg : fileList2) {
                File file = new File(uploadFolder + "/" + ebookImg.getSavefile());
                if (file.exists()) { // 해당 파일이 존재하면
                    file.delete(); // 파일 삭제

                    // 썸네일도 삭제
                    File thumbnailFile = new File(uploadFolder + "\\thumbs_" + ebookImg.getSavefile());
                    thumbnailFile.delete();
                }
                log.info(" ebookImg : " + ebookImg);
            }
            eBookService.removeFileAll(postNo);
            fileboard.setFileList(fileList); // 파일
            fileboard.setFileBoard(ebook); //글 제목 내용
            eBookService.updateFileboard(fileboard); // 모든 내용 업데이트
        } else { // 파일이 없는 경우
            eBookService.updateEbook(ebook); // 글 제목 내용만 업데이트
        }
        return "redirect:/admin/EbookList";
    }

    // 중학 교재

    @Autowired
    private MBookServiceImpl mBookService;

    @GetMapping("MbookList")
    public String MbookList(Model model) throws Exception{
        List<Mbook> mbookList = mBookService.admMbookList();

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

        return "admin/Mbook/MbookList";
    }

    @GetMapping("MbookInsert")
    public String MbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "admin/Mbook/MbookInsert";
    }

    @GetMapping("Mbook/fileUpload")
    public String fileUploadForm2() {
        return "admin/Mbook/MbookInsert";
    }

    @PostMapping("Mbook/fileUpload")
    public String fileUpload2(@RequestParam("files") List<MultipartFile> files,
                             @RequestParam Map<String, String> params,
                             HttpServletRequest req,
                             Model model) throws Exception {

        // Create the 'board' object
        Mbook mbook = new Mbook();
        mbook.setId(params.get("id"));
        mbook.setTitle(params.get("title"));
        mbook.setContent(params.get("content"));
        mbook.setServecontent(params.get("servecontent"));
        mbook.setPrice(Integer.valueOf(params.get("price")));
        mbook.setPublish(params.get("publish"));

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

                    // 썸네일 저장 위치경로 + 이름
                    // 썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
                    File thumbsname= new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);
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

        return "redirect:/admin/MbookList";
    }

    @PostMapping("MbookDelRec")  // 삭제, 복구
    public String MbookDelRec(Mbook book) {
        mBookService.mbookDelRec(book);
        return "redirect:/admin/MbookList";
    }

/*    @GetMapping("MbookDelete")
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
        return "redirect:/admin/MbookList";
    }*/

    // 수정 폼 이동
    @GetMapping("MbookUpdate")
    public String modifyFileboard2(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Mbook mbook = mBookService.getMbook(postNo);
        List<MbookImg> fileList = mBookService.getFileGroupList(postNo);
        model.addAttribute("Mbook", mbook);
        model.addAttribute("fileList", fileList);
        return "admin/Mbook/MbookUpdate";
    }

    @PostMapping("MbookUpdate")
    public String modifyFileboard3(@RequestParam("Mbno") Integer postNo,
                                   @RequestParam("files") List<MultipartFile> files,
                                   @RequestParam Map<String, String> params,
                                   HttpServletRequest req, Model model) throws Exception {
        MbookVO fileboard = new MbookVO();


        // Create the 'board' object
        Mbook mbook = new Mbook();
        mbook.setNo(postNo);
        mbook.setId(params.get("id"));
        mbook.setTitle(params.get("title"));
        mbook.setContent(params.get("content"));
        mbook.setServecontent(params.get("servecontent"));
        mbook.setPrice(Integer.valueOf(params.get("price")));
        mbook.setPublish(params.get("publish"));


        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" dispatcher-servlet에서 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);
        log.info(" mbook : " + mbook);


        //여러 파일 반복 저장
        List<MbookImg> fileList = new ArrayList<>();

        boolean checkFile = true;

        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                log.info(" file : " + file);

                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성


                MbookImg data = new MbookImg();

                data.setNo(postNo);
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());

                Date today = new Date();
                data.setUploaddate(today.toString());
                data.setMbno(postNo);
                fileList.add(data);
                File saveFile = new File(uploadFolder, saveFileName); //실제 파일 객체 생성
                log.info(" fileList : " + fileList);

                try {
                    file.transferTo(saveFile);  //실제 디렉토리에 해당파일 저장
//                file.transferTo(devFile); //개발자용 컴퓨터에 해당파일 저장
                    // 썸네일 저장 위치경로 + 이름
                    // 썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
                    File thumbsname= new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);

                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            } else {
                checkFile = false;
//                break;
            }
        }
        log.info(" checkFile1 : " + checkFile);
        if(checkFile == true) { // 파일이 있는 경우
            List<MbookImg> fileList2 = mBookService.getFileGroupList(postNo);
            for (MbookImg mbookImg : fileList2) {
                File file = new File(uploadFolder + "/" + mbookImg.getSavefile());
                if (file.exists()) { // 해당 파일이 존재하면
                    file.delete(); // 파일 삭제

                    // 썸네일도 삭제
                    File thumbnailFile = new File(uploadFolder + "\\thumbs_" + mbookImg.getSavefile());
                    thumbnailFile.delete();
                }
                log.info(" mbookImg : " + mbookImg);
            }
            mBookService.removeFileAll(postNo);
            fileboard.setFileList(fileList); // 파일
            fileboard.setFileBoard(mbook); //글 제목 내용
            mBookService.updateFileboard(fileboard); // 모든 내용 업데이트
        } else { // 파일이 없는 경우
            mBookService.updateMbook(mbook); // 글 제목 내용만 업데이트
        }
        return "redirect:/admin/MbookList";
    }

    // 수능 교재

    @Autowired
    private HBookServiceImpl hBookService;

    @GetMapping("HbookList")
    public String HbookList(Model model) throws Exception{
        List<Hbook> hbookList = hBookService.admHbookList();

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

        return "admin/Hbook/HbookList";
    }

    @GetMapping("HbookInsert")
    public String HbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "admin/Hbook/HbookInsert";
    }

    @GetMapping("Hbook/fileUpload")
    public String fileUploadForm3() {
        return "admin/Hbook/HbookInsert";
    }

    @PostMapping("Hbook/fileUpload")
    public String fileUpload3(@RequestParam("files") List<MultipartFile> files,
                              @RequestParam Map<String, String> params,
                              HttpServletRequest req,
                              Model model) throws Exception {

        // Create the 'board' object
        Hbook hbook = new Hbook();
        hbook.setId(params.get("id"));
        hbook.setTitle(params.get("title"));
        hbook.setContent(params.get("content"));
        hbook.setServecontent(params.get("servecontent"));
        hbook.setPrice(Integer.valueOf(params.get("price")));
        hbook.setPublish(params.get("publish"));

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

                    // 썸네일 저장 위치경로 + 이름
                    // 썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
                    File thumbsname= new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);

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

        return "redirect:/admin/HbookList";
    }

    @PostMapping("HbookDelRec")  // 삭제, 복구
    public String HbookDelRec(Hbook book) {
        hBookService.hbookDelRec(book);
        return "redirect:/admin/HbookList";
    }

    /*@GetMapping("HbookDelete")
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
        return "redirect:/admin/HbookList";
    }*/

    // 수정 폼 이동
    @GetMapping("HbookUpdate")
    public String modifyFileboard3(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Hbook hbook = hBookService.getHbook(postNo);
        List<HbookImg> fileList = hBookService.getFileGroupList(postNo);
        model.addAttribute("Hbook", hbook);
        model.addAttribute("fileList", fileList);
        return "admin/Hbook/HbookUpdate";
    }

    @PostMapping("HbookUpdate")
    public String modifyFileboard4(@RequestParam("Hbno") Integer postNo,
                                   @RequestParam("files") List<MultipartFile> files,
                                   @RequestParam Map<String, String> params,
                                   HttpServletRequest req, Model model) throws Exception {
        HbookVO fileboard = new HbookVO();


        // Create the 'board' object
        Hbook hbook = new Hbook();
        hbook.setNo(postNo);
        hbook.setId(params.get("id"));
        hbook.setTitle(params.get("title"));
        hbook.setContent(params.get("content"));
        hbook.setServecontent(params.get("servecontent"));
        hbook.setPrice(Integer.valueOf(params.get("price")));
        hbook.setPublish(params.get("publish"));


        log.info("-----------------------------------");
        log.info(" 현재 프로젝트 홈 : " + req.getContextPath());
        log.info(" dispatcher-servlet에서 지정한 경로 : " + uploadFolder);
        log.info(" 요청 URL : " + req.getServletPath());
        log.info(" 프로젝트 저장 경로 : " + uploadFolder);
        log.info(" hbook : " + hbook);


        //여러 파일 반복 저장
        List<HbookImg> fileList = new ArrayList<>();

        boolean checkFile = true;

        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                log.info(" file : " + file);

                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성


                HbookImg data = new HbookImg();

                data.setNo(postNo);
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());

                Date today = new Date();
                data.setUploaddate(today.toString());
                data.setHbno(postNo);
                fileList.add(data);
                File saveFile = new File(uploadFolder, saveFileName); //실제 파일 객체 생성
                log.info(" fileList : " + fileList);

                try {
                    file.transferTo(saveFile);  //실제 디렉토리에 해당파일 저장
//                file.transferTo(devFile); //개발자용 컴퓨터에 해당파일 저장
                    // 썸네일 저장 위치경로 + 이름
                    // 썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
                    File thumbsname= new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);
                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            } else {
                checkFile = false;
//                break;
            }
        }
        log.info(" checkFile1 : " + checkFile);
        if(checkFile == true) { // 파일이 있는 경우
            List<HbookImg> fileList2 = hBookService.getFileGroupList(postNo);
            for (HbookImg hbookImg : fileList2) {
                File file = new File(uploadFolder + "/" + hbookImg.getSavefile());
                if (file.exists()) { // 해당 파일이 존재하면
                    file.delete(); // 파일 삭제

                    // 썸네일도 삭제
                    File thumbnailFile = new File(uploadFolder + "\\thumbs_" + hbookImg.getSavefile());
                    thumbnailFile.delete();
                }
                log.info(" hbookImg : " + hbookImg);
            }
            hBookService.removeFileAll(postNo);
            fileboard.setFileList(fileList); // 파일
            fileboard.setFileBoard(hbook); //글 제목 내용
            hBookService.updateFileboard(fileboard); // 모든 내용 업데이트
        } else { // 파일이 없는 경우
            hBookService.updateHbook(hbook); // 글 제목 내용만 업데이트
        }
        return "redirect:/admin/HbookList";
    }

    // 토익 교재

    @Autowired
    private TBookServiceImpl tBookService;

    @GetMapping("TbookList")
    public String TbookList(Model model) throws Exception{
        List<Tbook> tbookList = tBookService.admTbookList();

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

        return "admin/Tbook/TbookList";
    }

    @GetMapping("TbookInsert")
    public String TbookInsertForm(@RequestParam("name") String name, Model model) throws Exception {
        model.addAttribute("name", name);
        return "admin/Tbook/TbookInsert";
    }

    @GetMapping("Tbook/fileUpload")
    public String fileUploadForm4() {
        return "admin/Tbook/TbookInsert";
    }

    @PostMapping("Tbook/fileUpload")
    public String fileUpload5(@RequestParam("files") List<MultipartFile> files,
                              @RequestParam Map<String, String> params,
                              HttpServletRequest req,
                              Model model) throws Exception {

        // Create the 'board' object
        Tbook tbook = new Tbook();
        tbook.setId(params.get("id"));
        tbook.setTitle(params.get("title"));
        tbook.setContent(params.get("content"));
        tbook.setServecontent(params.get("servecontent"));
        tbook.setPrice(Integer.valueOf(params.get("price")));
        tbook.setPublish(params.get("publish"));

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

                    // 썸네일 저장 위치경로 + 이름
                    // 썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
                    File thumbsname= new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);
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

        return "redirect:/admin/TbookList";
    }

    @PostMapping("TbookDelRec")  // 삭제, 복구
    public String tbookDelRec(Tbook book) {
        tBookService.tbookDelRec(book);
        return "redirect:/admin/TbookList";
    }

    /*@GetMapping("TbookDelete")
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
        return "redirect:/admin/TbookList";
    }*/

    // 수정 폼 이동
    @GetMapping("TbookUpdate")
    public String modifyFileboard4(@RequestParam("no") Integer postNo, Model model) throws Exception {
        Tbook tbook = tBookService.getTbook(postNo);
        List<TbookImg> fileList = tBookService.getFileGroupList(postNo);
        model.addAttribute("Tbook", tbook);
        model.addAttribute("fileList", fileList);
        return "admin/Tbook/TbookUpdate";
    }

    @PostMapping("TbookUpdate")
    public String modifyFileboard5(@RequestParam("Tbno") Integer postNo,
                                   @RequestParam("files") List<MultipartFile> files,
                                   @RequestParam Map<String, String> params,
                                   HttpServletRequest req, Model model) throws Exception {
        TbookVO fileboard = new TbookVO();


        // Create the 'board' object
        Tbook tbook = new Tbook();
        tbook.setNo(postNo);
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
        log.info(" tbook : " + tbook);


        //여러 파일 반복 저장
        List<TbookImg> fileList = new ArrayList<>();

        boolean checkFile = true;

        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                log.info(" file : " + file);

                // 파일 처리 로직 시작
                String randomUUID = UUID.randomUUID().toString();  // 파일 이름 중복 방지를 위한 랜덤 UUID 생성
                String OriginalFilename = file.getOriginalFilename();  // 실제 파일 이름
                String Extension = OriginalFilename.substring(OriginalFilename.lastIndexOf("."));  // 파일 확장자 추출
                String saveFileName = randomUUID + Extension;  // 저장할 파일 이름 생성


                TbookImg data = new TbookImg();

                data.setNo(postNo);
                data.setSavefolder(uploadFolder);
                data.setOriginfile(file.getOriginalFilename());
                data.setSavefile(saveFileName);
                data.setFilesize(file.getSize());

                Date today = new Date();
                data.setUploaddate(today.toString());
                data.setTbno(postNo);
                fileList.add(data);
                File saveFile = new File(uploadFolder, saveFileName); //실제 파일 객체 생성
                log.info(" fileList : " + fileList);

                try {
                    file.transferTo(saveFile);  //실제 디렉토리에 해당파일 저장
//                file.transferTo(devFile); //개발자용 컴퓨터에 해당파일 저장
                    // 썸네일 저장 위치경로 + 이름
                    // 썸네일 생성 (복사할파일위치,썸네일생성경로,가로,세로)
                    File thumbsname= new File(uploadFolder+"\\thumbs_"+saveFileName);
                    Thumbnailator.createThumbnail(saveFile, thumbsname,150, 150);

                } catch (IllegalStateException | IOException e) {
                    e.printStackTrace();
                    // 예외 처리
                }
            } else {
                checkFile = false;
//                break;
            }
        }
        log.info(" checkFile1 : " + checkFile);
        if(checkFile == true) { // 파일이 있는 경우
            List<TbookImg> fileList2 = tBookService.getFileGroupList(postNo);
            for (TbookImg tbookImg : fileList2) {
                File file = new File(uploadFolder + "/" + tbookImg.getSavefile());
                if (file.exists()) { // 해당 파일이 존재하면
                    file.delete(); // 파일 삭제

                    // 썸네일도 삭제
                    File thumbnailFile = new File(uploadFolder + "\\thumbs_" + tbookImg.getSavefile());
                    thumbnailFile.delete();
                }
                log.info(" tbookImg : " + tbookImg);
            }
            tBookService.removeFileAll(postNo);
            fileboard.setFileList(fileList); // 파일
            fileboard.setFileBoard(tbook); //글 제목 내용
            tBookService.updateFileboard(fileboard); // 모든 내용 업데이트
        } else { // 파일이 없는 경우
            tBookService.updateTbook(tbook); // 글 제목 내용만 업데이트
        }
        return "redirect:/admin/TbookList";
    }

}