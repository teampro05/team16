package com.pro06.controller.course;

import com.pro06.entity.Course;
import com.pro06.entity.LecAns;
import com.pro06.entity.LecTest;
import com.pro06.entity.Lecture;
import com.pro06.service.course.LectureServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// 강의 컨트롤러

@Controller
@Log4j2
@RequestMapping("/lecture/*")
public class LectureController {

    // 실제 업로드 디렉토리
    // thymeleaf 에서는 외부에 지정하여 사용해야 한다.
    // jsp와는 다르게 webapp이 없기 때문이다.
    // resources는 정적이라 업데이트 되어도 파일을 못 찾기에 서버를 재 시작 해야함
    @Value("${spring.servlet.multipart.location}")
    String uploadFolder;

    @Autowired
    private LectureServiceImpl lectureService;

    // 시험 보기
    @PostMapping("test")
    public String test(@RequestParam("cno") Integer cno,
                       @RequestParam("lno") Integer lno,
                       Principal principal, Model model){

        LecTest lecTest = lectureService.getLecTest(cno, lno);
        
        // 문제 저장
        List<String> examList = new ArrayList<>();
        examList.add(lecTest.getExam1());
        examList.add(lecTest.getExam2());
        examList.add(lecTest.getExam3());
        examList.add(lecTest.getExam4());
        examList.add(lecTest.getExam5());
        
        // 선택 답안 저장
        List<String> itemList = new ArrayList<>();
        itemList.add(lecTest.getAnswer1());
        itemList.add(lecTest.getAnswer2());
        itemList.add(lecTest.getAnswer3());
        itemList.add(lecTest.getAnswer4());
        itemList.add(lecTest.getAnswer5());
        Collections.shuffle(itemList);

        model.addAttribute("cno", cno);
        model.addAttribute("lno", lno);
        model.addAttribute("examList", examList);
        model.addAttribute("itemList", itemList);
        model.addAttribute("lecTest", lecTest);
        return "lecture/lectureTest";
    }

    // 정답 가져오기
    @PostMapping("answers")
    @ResponseBody
    public List<String> answers(@RequestParam("cno") Integer cno,
                                @RequestParam("lno") Integer lno) throws Exception {
        LecTest lecTest = lectureService.getLecTest(cno, lno);
        List<String> answers = new ArrayList<>();
        answers.add(lecTest.getAnswer1());
        answers.add(lecTest.getAnswer2());
        answers.add(lecTest.getAnswer3());
        answers.add(lecTest.getAnswer4());
        answers.add(lecTest.getAnswer5());
        return answers;
    }
    
    // 답안 전송
    @PostMapping("lecAns")
    public void lecAns(Principal principal,
                       @RequestParam("cno") Integer cno,
                       @RequestParam("lno") Integer lno,
                       LecAns lecAns, HttpServletResponse res) throws Exception {
        String id = principal.getName();

        LecAns lecAns1 = lectureService.getLecAns(cno, lno, id);

        // 강좌 번호
        Course course = new Course();
        course.setNo(cno);
        lecAns.setCourse(course);

        // 강의 번호
        Lecture lecture = new Lecture();
        lecture.setNo(lno);
        lecAns.setLecture(lecture);

        // 아이디
        lecAns.setId(id);

        lectureService.lecAnsInsUpd(lecAns);

        // 동영상 플레이어 창 닫기
        res.setContentType("text/html; charset=UTF-8");
        PrintWriter out = res.getWriter();
        out.println("<script>window.close();</script>");
        out.flush();
    }
}
