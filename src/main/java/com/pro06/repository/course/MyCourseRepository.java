package com.pro06.repository.course;


import com.pro06.entity.MyCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyCourseRepository extends JpaRepository<MyCourse, Integer> {
    
    // 내 강좌 목록 보기
    @Query("select mc from MyCourse mc where mc.id = :id")
    List<MyCourse> myCourseList(@Param("id") String id);
    
    // 해당 유저가 해당 강좌를 수강하는지 확인
    @Query("select count(mc) from MyCourse mc where mc.id = :id and mc.course.no = :cno")
    Integer getMyCourse(@Param("id") String id, @Param("cno") Integer cno);

    // 해당 강좌를 몇 명이 듣고 있는지 확인
    @Query("select count(mc) from MyCourse mc where mc.state = 'y' and mc.course.no = :cno")
    Integer getMyCourseCno(@Param("cno") Integer cno);
}
