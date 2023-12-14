/*
package com.pro06.repository.Book;

import com.pro06.entity.Book;
import com.pro06.entity.BookImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookImgRepository extends JpaRepository<BookImg, Integer> {
    @Query("select i from BookImg i where i.book.no = :bno")
    List<BookImg> bookImgList(@Param("bno") Integer bno);

    @Query("select i from BookImg i where i.no = :no")
    BookImg getBookImg(@Param("no") Integer no);
}
*/
