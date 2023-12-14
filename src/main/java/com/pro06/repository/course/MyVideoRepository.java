package com.pro06.repository.course;


import com.pro06.entity.MyVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MyVideoRepository extends JpaRepository<MyVideo, Integer> {
    
    // 내가본 강의 영상 정보 확인
//    @Query("select mv from MyVideo mv where mv.id = :id and mv.course.no = :cno and mv.lecture.no = :lno")
//    List<MyCourse> myVideoList(@Param("id") String id, @Param("cno") Integer cno, @Param("lno") Integer lno);

    // 해당 강의 영상정보가 있는지 확인
    @Query("select count(mv) from MyVideo mv where mv.id = :id and mv.course.no = :cno and mv.lecture.no = :lno")
    Integer getMyVideoCnt(@Param("id") String id, @Param("cno") Integer cno, @Param("lno") Integer lno);

    // 해당 강의 영상정보가 있는지 확인
    @Query("select mv from MyVideo mv where mv.id = :id and mv.course.no = :cno and mv.lecture.no = :lno")
    MyVideo getMyVideo(@Param("id") String id, @Param("cno") Integer cno, @Param("lno") Integer lno);

    // 동영상 시청 위치 저장
    @Modifying
    @Query("update MyVideo mv set mv.page = :#{#myVideo.page}, mv.sec = :#{#myVideo.sec} where mv.course.no = :#{#myVideo.course.getNo()} and mv.lecture.no = :#{#myVideo.lecture.getNo()} and mv.id = :#{#myVideo.id}")
    void updatePageSec(@Param("myVideo") MyVideo myVideo);

}
