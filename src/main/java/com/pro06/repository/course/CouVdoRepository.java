package com.pro06.repository.course;

import com.pro06.entity.course.CourseVideo;
import com.pro06.entity.course.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouVdoRepository extends JpaRepository<CourseVideo, Integer> {

    // 해당 강좌의 해당 강의의 실제 저장된 비디오 이름을 List 형태로 추출
    @Query("select v from CourseVideo v where v.course.no = :cno")
    Optional<CourseVideo> getCouVideo(@Param("cno") Integer cno);
}