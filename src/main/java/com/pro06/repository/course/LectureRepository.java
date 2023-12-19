package com.pro06.repository.course;


import com.pro06.entity.course.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Integer> {
    // 유저용
    // cno로 강의 리스트 정보 추출
    @Query("select l from Lecture l where l.course.no = :cno and l.deleteYn = 'n'")
    List<Lecture> lectureCnoList(Integer cno);
    
    // 관리자용
    // 강의 리스트 정보 추출
    @Query("select l from Lecture l where l.course.no = :cno")
    List<Lecture> admLectureList(Integer cno);
}
