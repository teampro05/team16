package com.pro06.repository;

import com.pro06.entity.Faq;
import com.pro06.entity.Notice;
import com.pro06.entity.User;
import com.pro06.entity.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Integer> {

    @Query("select m from Notice m where m.no = :no")
    Notice get(@Param("no") Integer no);

    @Query("select n from Notice n where n.deleteyn = 'n'")
    List<Notice> usernoticeList();

}
