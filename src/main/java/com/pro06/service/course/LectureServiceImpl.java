package com.pro06.service.course;

import com.pro06.dto.LectureVO;
import com.pro06.entity.*;
import com.pro06.repository.course.LecAnsRepository;
import com.pro06.repository.course.LecTestRepository;
import com.pro06.repository.course.LectureRepository;
import com.pro06.repository.course.VideoRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@Log4j2
public class LectureServiceImpl {
    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private LecTestRepository lecTestRepository;

    @Autowired
    private LecAnsRepository lecAnsRepository;

    // 강의 정보 + 파일 등록
    public void LectureVoInsert(LectureVO vo) throws Exception {
        Lecture lecture = vo.getLecture();
        List<Video> fileList = vo.getFileList();
        
        // 강의 정보 등록
        Course course = new Course();
        course.setNo(vo.getCno());
        lecture.setCourse(course);
        lectureRepository.save(lecture);

        log.info("lecture 저장");
        
        // 강의 영상 등록
        for(Video video: fileList) {
            video.setLecture(lecture);
            video.setCourse(course);
            videoRepository.save(video);
            log.info("video 저장");
        }
        
        // 시험 정보 등록
        LecTest lecTest = vo.getLecTest();
        lecTest.setLecture(lecture);
        lecTest.setCourse(course);
        lecTestRepository.save(lecTest);
    }

    // 해당강의의 강좌 목록 불러오기
    public List<Lecture> lectureCnoList(Integer cno) {
        return lectureRepository.lectureCnoList(cno);
    }
    
    // 시험 정보 가져오기
    public LecTest getLecTest(Integer cno, Integer lno) {return lecTestRepository.getLecTest(cno, lno);}
    
    // 시험 제출 답안 입력, 수정
    public LecAns lecAnsInsUpd(LecAns lecAns) {
//        lecAnsRepository.findById();
        return lecAnsRepository.save(lecAns);
    }
    
    // 제출된 답안 수정
    public LecAns lecAnsUpdate(LecAns lecAns) {return lecAnsRepository.save(lecAns);}
    
    // 답안 정보 추출
    public LecAns getLecAns(Integer cno, Integer lno, String id) {
        return lecAnsRepository.getLecAns(cno, lno, id);
    }
}
