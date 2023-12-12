package com.pro06.repository.course;


import com.pro06.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    
    // 수강생이 다 차지 않은 강좌만 불러오기
    @Query("select c from Course c where c.peo < c.peo_max")
    List<Course> courseList();
}
