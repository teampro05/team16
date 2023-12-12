package com.pro06.repository.course;


import com.pro06.entity.LecTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LecTestRepository extends JpaRepository<LecTest, Integer> {
    // 시험 정보 가져오기
    @Query("select lt from LecTest lt where lt.lecture.no = :lno and lt.course.no = :cno")
    LecTest getLecTest(@Param("cno") Integer cno, @Param("lno") Integer lno);

}
