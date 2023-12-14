package com.pro06.repository;

import com.pro06.entity.Faq;
import com.pro06.entity.Notice;
import com.pro06.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("select m from Notice m where m.no = :no")
    Notice get(@Param("no") Long no);

}
