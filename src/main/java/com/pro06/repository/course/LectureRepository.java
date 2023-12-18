package com.pro06.repository.course;


import com.pro06.entity.course.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    // cno로 강의 리스트 정보 추출
    @Query("select l from Lecture l where l.course.no = :cno")
    List<Lecture> lectureCnoList(Integer cno);

    // 해당 강좌, 강의 영상 정보 추출
    @Query("select l from Lecture l where l.no = :lno and l.course.no = :cno")
    Lecture videoList(@Param("cno") Integer cno, @Param("lno") Integer lno);
}
