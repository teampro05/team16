package com.pro06.controller.course;

import com.pro06.dto.CourseDto;
import com.pro06.dto.LecQueDto;
import com.pro06.dto.LectureDto;
import com.pro06.dto.MyVideoDto;
import com.pro06.service.course.LectureServiceImpl;
import com.pro06.service.course.MyCourseServiceImpl;
import com.pro06.service.course.MyVideoServiceImpl;
import com.pro06.service.course.VideoServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/video/*")
public class VideoController {

    @Autowired
    private VideoServiceImpl videoService;

    @Autowired
    private MyVideoServiceImpl myVideoService;

    @Autowired
    private MyCourseServiceImpl myCourseService;

    @Autowired
    private LectureServiceImpl lectureService;
    
    // 영상 재생
    // 아이디, 강좌번호, 강의번호, 현재페이지를 받음
    @PostMapping("player")
    public String player(Principal principal,
                         @RequestParam("cno") Integer cno,
                         @RequestParam("lno") Integer lno,
                         @RequestParam("page") Integer page, Model model) {

        if(principal == null) {
            log.error("잘 못된 접근입니다.");
            return "redirect:/";
        }

        String id = principal.getName();
        
        // 해당 강의를 수강하는 사람인지 확인
        Integer ck1 = myCourseService.getMyCourseCnt(id, cno);
        if(ck1 == 0) {
            log.error("고객님은 해당 강의를 수강하고 있지 않습니다.");
            return "redirect:/";
        }
        
        // 수강중인 강의 시간 저장 데이터 생성
        Integer ck2 = myVideoService.getMyVideoCnt(id, cno, lno);
        if(ck2 == 0) { // 만약 회원의 해당 강의 영상 정보가 없으면 생성
            MyVideoDto myVideo = new MyVideoDto();
            myVideo.setId(id);  // 회원의 아이디 저장

            LectureDto lecture = new LectureDto();
            lecture.setNo(lno); // 강의번호 저장
            myVideo.setLecture(lecture);

            CourseDto course = new CourseDto();
            course.setNo(cno);  // 강좌번호 저장
            myVideo.setCourse(course);

            myVideoService.myVideoInsert(myVideo); // 데이터 생성
        }
        
        // page, sec(시간) 처리
        MyVideoDto myVideo = myVideoService.getMyVideo(id, cno, lno);
        Integer userPage = 0;
        Integer userSec = 0;
        if(myVideo != null) {
            userPage = myVideo.getPage();
            // page 검사
            if(userPage != null && userPage < page) {
                // 동영상 시청 위치 저장
                myVideo.setSec(0);
                myVideo.setPage(page);
                myVideo.setState("n");
                myVideoService.updateSecPageState(myVideo);
            }
            userSec = myVideo.getSec();
        }


        // 질문 목록 출력
        LecQueDto lecQueDto = new LecQueDto();
        lecQueDto.setId(id);
        lecQueDto.setPage(page);
        CourseDto cou = new CourseDto();
        cou.setNo(cno);
        lecQueDto.setCourse(cou);
        LectureDto lec = new LectureDto();
        lec.setNo(lno);
        lecQueDto.setLecture(lec);
        List<LecQueDto> queList = lectureService.lecQueList(lecQueDto);


        // 영상의 파일 이름 출력
        List<String> videoList = videoService.videoList(cno, lno);

        model.addAttribute("que_size", queList.size());         // 질문 리스트 수
        model.addAttribute("queList", queList);                 // 해당 영상의 질문 목록
        model.addAttribute("cno", cno);                         // 강좌번호
        model.addAttribute("lno", lno);                         // 강의 번호
        model.addAttribute("total_size", videoList.size());     //
        model.addAttribute("user_page", userPage);              // 수강하는 사람한테 저장되있는 페이지
        model.addAttribute("page", page);                       // 현재 영상 페이지
        model.addAttribute("sec", userSec);                     // 들은 시간
        model.addAttribute("state", myVideo.getState());        // 완강 상태
        model.addAttribute("savefile", videoList.get(page));
        return "video/player";
    }
    
    // 수강 종료 (해당 강의의 모든 영상을 다 봄)
    @PostMapping("complete")
    public void summary(Principal principal, HttpServletResponse res,
                        @RequestParam("cno") Integer cno,
                        @RequestParam("lno") Integer lno,
                        @RequestParam("page") Integer page) throws IOException {

        if(principal == null) {
            log.error("잘 못된 접근입니다.");
        }

        String id = principal.getName();

        MyVideoDto myVideo = myVideoService.getMyVideo(id, cno, lno);
        Integer userPage = 0;
        if(myVideo != null) {
            userPage = myVideo.getPage();
            // page 검사
            if(userPage < page) {
                // 동영상 시청 위치 저장
                myVideo.setSec(0);
                myVideo.setPage(page);
                myVideo.setState("y");
                myVideoService.updateSecPageState(myVideo);
            }
        }
        
        // 동영상 플레이어 창 닫기
        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<script>alert('시험을 안보신 분은 시험을 봐야 수강처리가 인정됩니다.')</script>");
        out.println("<script>window.close();</script>");
        out.flush();
    }
    
    // 동영상에서 나간 경우
    // 여기 트랜잭션 처리 안할경우 오류 발생
    @PostMapping("closeVideo")
    @Transactional
    public void closeLecture(Principal principal,
                             @RequestParam("sec") Integer sec,
                             @RequestParam("page") Integer page,
                             @RequestParam("cno") Integer cno,
                             @RequestParam("lno") Integer lno, Model model) throws Exception {

        String id = principal.getName();

        log.warn("sec : " + sec);
        log.warn("page : " + page);
        log.warn("cno : " + cno);
        log.warn("lno : " + lno);
        log.warn("id : " + id);
        
        // 영상 시청 정보 저장
        MyVideoDto myVideo = new MyVideoDto();
        myVideo.setSec(sec);
        myVideo.setPage(page);

        CourseDto course = new CourseDto();
        course.setNo(cno);
        myVideo.setCourse(course);

        LectureDto lecture = new LectureDto();
        lecture.setNo(lno);
        myVideo.setLecture(lecture);

        myVideo.setId(id);
        
        log.warn("영상 시청 정보 수정");
        
        // 동영상 시청 위치 저장
        myVideoService.updateSecPage(myVideo);

        log.warn("영상 시청 정보 수정완료");
    }
}
