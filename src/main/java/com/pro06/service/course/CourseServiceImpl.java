package com.pro06.service.course;

import com.pro06.entity.Course;
import com.pro06.repository.course.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CourseServiceImpl {
    @Autowired
    private CourseRepository courseRepository;
    
    // 강좌 등록
    public Course courseInsert(Course course) {
        return courseRepository.save(course);
    }
    
    // 어드민 강좌 목록 불러오기
    public List<Course> admCourseList() {
        return courseRepository.findAll();
    }

    // 강좌 목록 불러오기
    public List<Course> courseList() {
        return courseRepository.courseList();
    }
    
    // 강좌 상세 보기
    public Course getCourse(Integer no) {return courseRepository.getReferenceById(no);}

    // 강좌 수강신청 수강생 +1
    public void setCoursePeo(Integer no) {
        Optional<Course> course = courseRepository.findById(no);
        course.get().setPeo(course.get().getPeo()+1);
    }
}
