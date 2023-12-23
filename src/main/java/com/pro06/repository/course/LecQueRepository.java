package com.pro06.repository.course;

import com.pro06.entity.course.LecQue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecQueRepository extends JpaRepository<LecQue, Integer> {
    // 강의 영상에서 확인
    @Query("select lq from LecQue lq where lq.lecture.no = :lno and lq.course.no = :cno and lq.id = :id and lq.page = :page and lq.deleteYn = :deleteYn")
    List<LecQue> lecQueList(String id, Integer page, Integer cno, Integer lno, String deleteYn);
    
    // 질문 목록에서 확인
    @Query("select lq from LecQue lq where lq.lecture.no = :lno and lq.course.no = :cno and lq.id = :id and lq.deleteYn = 'n'")
    List<LecQue> myLecQueList(Integer cno, Integer lno, String id);
}
