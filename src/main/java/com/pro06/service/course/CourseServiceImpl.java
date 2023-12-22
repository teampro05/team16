package com.pro06.service.course;

import com.pro06.dto.course.CourseDto;
import com.pro06.dto.course.CourseVideoDto;
import com.pro06.entity.course.Course;
import com.pro06.repository.course.CourseRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CourseServiceImpl {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    // 강좌 등록
    public CourseDto courseInsert(CourseDto courseDto) {
        Course course = modelMapper.map(courseDto, Course.class);
        Course res = courseRepository.save(course);
        return modelMapper.map(res, CourseDto.class);
    }

    // 강좌 수정
    public void courseUpdate(CourseDto courseDto) {
        Optional<Course> course = courseRepository.findById(courseDto.getNo());
        Course res = course.orElseThrow();
        res.change(courseDto.getId(), courseDto.getLevel(),
                courseDto.getTitle(), courseDto.getContent(),
                courseDto.getCopen());
        courseRepository.save(res);
    }

    // 강좌 삭제, 복구
    public void couDelRec(CourseDto dto) {
        Optional<Course> course = courseRepository.findById(dto.getNo());
        Course res = course.orElseThrow();
        res.delete(dto.getDeleteYn());
        courseRepository.save(res);
    }
    
    // 어드민 강좌 목록 불러오기
    public List<CourseDto> admCourseList() {
        List<Course> lst = courseRepository.findAll();
        List<CourseDto> courseList = lst.stream().map(course ->
                modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
        return courseList;
    }

    // 강좌 목록 불러오기
    public List<CourseDto> courseList() {
        List<Course> lst = courseRepository.courseList();
        List<CourseDto> courseList = lst.stream().map(course ->
                modelMapper.map(course, CourseDto.class))
                .collect(Collectors.toList());
        return courseList;
    }
    
    // 강좌 상세 보기
    public CourseDto getCourse(Integer no) {
        Optional<Course> course = courseRepository.findById(no);
        CourseDto courseDto = modelMapper.map(course, CourseDto.class);
        return courseDto;
    }

    // 강좌 수강신청 수강생 +1
    public void setCoursePeo(Integer no) {
        Optional<Course> course = courseRepository.findById(no);
        Course res = course.orElseThrow();
        res.peoUp();
        courseRepository.save(res);
    }
}
