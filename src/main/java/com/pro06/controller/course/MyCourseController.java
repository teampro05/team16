package com.pro06.controller.course;

import com.pro06.entity.Course;
import com.pro06.entity.Lecture;
import com.pro06.entity.MyCourse;
import com.pro06.entity.MyVideo;
import com.pro06.service.UserService;
import com.pro06.service.course.CourseServiceImpl;
import com.pro06.service.course.LectureServiceImpl;
import com.pro06.service.course.MyCourseServiceImpl;
import com.pro06.service.course.MyVideoServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@Log4j2
@RequestMapping("/mycourse/*")
public class MyCourseController {

    @Autowired
    private MyCourseServiceImpl myCourseService;

    @Autowired
    private LectureServiceImpl lectureService;

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private MyVideoServiceImpl myVideoService;

    @Autowired
    private UserService userService;

    // 내 강좌 목록
    @GetMapping("list")
    public String myCourseList(Principal principal, Model model) {
        String id = principal.getName();
        
        // 내가 수강 신청한 강좌 목록 추출
        List<MyCourse> myCourseList = myCourseService.myCourseList(id);
        
        // 테스트
/*        for (MyCourse mc: myCourseList) {
            System.out.println("mc.getCourse().getNo() : " + mc.getCourse().getNo());
            System.out.println("mc.getCourse().getTitle() : " + mc.getCourse().getTitle());
            System.out.println("mc.getCourse().getLevel() : " + mc.getCourse().getLevel());
            System.out.println("mc.getCourse().getContent() : " + mc.getCourse().getContent());
        }*/

        model.addAttribute("myCourseList", myCourseList);
        return "mycourse/myCourseList"; // 인덱스 이동
    }

    // 강좌 상세
    @GetMapping("detail")
    public String courseDetail(Principal principal, @RequestParam("no") Integer no, Model model) throws IOException {

        if(principal == null) {
            log.error("로그인을 해야 강의를 들을 수 있습니다.");
            return "redirect:/";
        }

        String id = principal.getName();

        Integer ck = myCourseService.getMyCourseCnt(id, no);
        if(ck == 0) {
            log.error("해당 강좌를 듣는 수강생이 아닙니다.");
            return "redirect:/";
        }

        // 강좌 상세
        Course course = courseService.getCourse(no);
        model.addAttribute("course", course);

        // 강의 목록
        List<Lecture> lectureList = lectureService.lectureCnoList(no);
        model.addAttribute("lectureList", lectureList);
        
        // 수강상태
        // 동영상을 다 봤는지 안봤는지 검사
        List<String> stateList = new ArrayList<>();
        for (Lecture lec: lectureList) {
            MyVideo myVideo = myVideoService.getMyVideo(id, no, lec.getNo());
            if(myVideo != null && myVideo.getState().equals("y")) {
                stateList.add("수강완료");
            } else {
                stateList.add("수강중");
            }
        }
        model.addAttribute("stateList", stateList);
        return "mycourse/myCourseDetail";
    }
}
