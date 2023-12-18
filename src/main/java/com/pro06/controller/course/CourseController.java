package com.pro06.controller.course;

import com.pro06.dto.course.CourseDto;
import com.pro06.dto.course.LectureDto;
import com.pro06.dto.course.MyCourseDto;
import com.pro06.entity.course.Course;
import com.pro06.entity.Status;
import com.pro06.entity.User;
import com.pro06.service.UserService;
import com.pro06.service.course.CourseServiceImpl;
import com.pro06.service.course.LectureServiceImpl;
import com.pro06.service.course.MyCourseServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 해당 컨트롤러에서 사용한 log.error들은 싹다 alert로 나중에 바꿔야함

// 강좌 컨트롤
@Component
@Controller
@Log4j2
@RequestMapping("/course/*")
public class CourseController {

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private LectureServiceImpl lectureService;

    @Autowired
    private MyCourseServiceImpl myCourseService;

    @Autowired
    private UserService userService;
    
    // 강좌 목록
    @GetMapping("list")
    public String courseList(Principal principal, Model model) {
        List<CourseDto> courseList = courseService.courseList();

        List<String> checkList = new ArrayList<>();
        
        // 로그인한 사람이면 강좌마다 수강신청 했는지 안했는지 검사
        if(principal != null) {
            String id = principal.getName();
            for (CourseDto course: courseList) {
                Integer cnt = myCourseService.getMyCourseCnt(id, course.getNo());
                if(cnt > 0) {
                    checkList.add("y");
                } else {
                    checkList.add("n");
                }
            }
        }

        model.addAttribute("checkList", checkList);
        model.addAttribute("courseList", courseList);
        return "course/courseList";
    }
    
    // 강좌 상세
    @GetMapping("detail")
    public String courseDetail(@RequestParam("no") Integer no, Model model) throws IOException {

        // 강좌 상세
        CourseDto course = courseService.getCourse(no);
        model.addAttribute("course", course);
        
        // 강의 목록
        List<LectureDto> lectureList = lectureService.lectureCnoList(no);
        model.addAttribute("lectureList", lectureList);
        return "course/courseDetail";
    }

    // 강좌 수강신청
    @GetMapping("apply")
    public String courseApply(Principal principal, @RequestParam("cno") Integer cno) {

        if(principal == null) {
            log.error("로그인을 해야 수강신청을 할 수 있습니다.");
            return "redirect:/";
        }

        String id = principal.getName();

        Integer ck = myCourseService.getMyCourseCnt(id, cno);

        if(ck > 0) {
            log.error("이미 해당 강의를 수강하고 있습니다.");
            return "redirect:/";
        }
        
        // 수강 신청 하기전에 인원이 다 찼는지 안 찼는지 확인
        CourseDto course1 = courseService.getCourse(cno);

        if(course1.getPeo() >= course1.getPeo_max() ) {
            log.error("이미 수강생이 다 찼습니다.");
            return "redirect:/";
        }

        CourseDto course = new CourseDto();
        course.setNo(cno);

        MyCourseDto myCourse = new MyCourseDto();
        myCourse.setId(id);
        myCourse.setCourse(course);

        courseService.setCoursePeo(cno);
        myCourseService.myCourseInsert(myCourse);

        return "redirect:/mycourse/list"; // 인덱스 이동
    }



}
