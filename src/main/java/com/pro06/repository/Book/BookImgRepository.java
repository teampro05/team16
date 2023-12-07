package com.pro06.repository.Book;

import com.pro06.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookImgRepository extends JpaRepository<Book, Integer> {
    @Query("select i.savefile from BookImg i where i.book.no = :bno")
    List<String> bookImgList(@Param("bno") Integer bno);
}
