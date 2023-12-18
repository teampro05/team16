package com.pro06.service.course;

import com.pro06.dto.course.MyCourseDto;
import com.pro06.entity.course.MyCourse;
import com.pro06.repository.course.MyCourseRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// 내 강좌
@Service
@Transactional
public class MyCourseServiceImpl {

    @Autowired
    private MyCourseRepository myCourseRepository;

    @Autowired
    private ModelMapper modelMapper;

    // 내 강좌 등록
    public void myCourseInsert(MyCourseDto courseDto) {
        MyCourse myCourse = modelMapper.map(courseDto, MyCourse.class);
        myCourseRepository.save(myCourse);
    }

    // 내 강좌 목록
    public List<MyCourseDto> myCourseList(String id) {
        List<MyCourse> lst = myCourseRepository.myCourseList(id);
        List<MyCourseDto> myCourseDtos = lst.stream().map(myCourse ->
                modelMapper.map(myCourse, MyCourseDto.class))
                .collect(Collectors.toList());
        return myCourseDtos;
    }

    // 해당 유저가 해당 강좌를 수강하는지 확인
    public Integer getMyCourseCnt(String id, Integer cno) { return myCourseRepository.getMyCourse(id, cno); }

    // 해당 강좌를 몇 명이 듣고 있는지 확인
    public Integer getMyCourseCno(Integer cno) {
        return myCourseRepository.getMyCourseCno(cno);
    }

}
