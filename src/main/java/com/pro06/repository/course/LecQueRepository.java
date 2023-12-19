package com.pro06.repository.course;

import com.pro06.entity.course.Course;
import com.pro06.entity.course.LecQue;
import com.pro06.entity.course.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecQueRepository extends JpaRepository<LecQue, Integer> {
    @Query("select lq from LecQue lq where lq.lecture.no = :lno and lq.course.no = :cno and lq.id = :id and lq.page = :page and lq.deleteYn = :deleteYn")
    List<LecQue> lecQueList(String id, Integer page, Integer cno, Integer lno, String deleteYn);
}
