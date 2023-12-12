package com.pro06.service.course;

import com.pro06.entity.MyCourse;
import com.pro06.repository.course.MyCourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.List;

// 내 강좌
@Service
@Transactional
public class MyCourseServiceImpl {

    @Autowired
    private MyCourseRepository myCourseRepository;

    // 내 강좌 등록
    public MyCourse myCourseInsert(MyCourse course) {
        return myCourseRepository.save(course);
    }

    // 내 강좌 목록
    public List<MyCourse> myCourseList(String id) {
        return myCourseRepository.myCourseList(id);
    }

    // 해당 유저가 해당 강좌를 수강하는지 확인
    public Integer getMyCourseCnt(String id, Integer cno) { return myCourseRepository.getMyCourse(id, cno); }

    // 해당 강좌를 몇 명이 듣고 있는지 확인
    public Integer getMyCourseCno(@Param("cno") Integer cno) {
        return myCourseRepository.getMyCourseCno(cno);
    }

}
