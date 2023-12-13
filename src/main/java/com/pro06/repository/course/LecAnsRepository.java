package com.pro06.repository.course;


import com.pro06.entity.LecAns;
import com.pro06.entity.MyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LecAnsRepository extends JpaRepository<LecAns, Integer> {
    // 제출된 정보 가져오기
    @Query("select la from LecAns la where la.lecture.no = :lno and la.course.no = :cno and la.id = :id")
    Optional<LecAns> getLecAns(@Param("cno") Integer cno, @Param("lno") Integer lno, @Param("id") String id);

    // 제출된 정보 수정
    @Modifying
    @Query("update MyVideo mv set mv.page = :#{#myVideo.page}, mv.sec = :#{#myVideo.sec} where mv.course.no = :#{#myVideo.course.getNo()} and mv.lecture.no = :#{#myVideo.lecture.getNo()} and mv.id = :#{#myVideo.id}")
    void updateLecAns(@Param("myVideo") MyVideo myVideo);
}
