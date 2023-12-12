package com.pro06.repository.course;

import com.pro06.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepository extends JpaRepository<Video, Integer> {
    
    // 해당 강좌의 해당 강의의 실제 저장된 비디오 이름을 List 형태로 추출
    @Query("select v.savefile from Video v where v.lecture.no = :lno and v.course.no = :cno")
    List<String> videoList(@Param("cno") Integer cno, @Param("lno") Integer lno);
}