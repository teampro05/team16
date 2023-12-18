package com.pro06.repository.course;


import com.pro06.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {
    
    // 수강생이 다 차지 않고 삭제 되지 않고 개설된 경우의 강좌만 불러오기
    @Query("select c from Course c where c.peo < c.peo_max and c.deleteYn = 'n' and c.copen = 1")
    List<Course> courseList();
}
