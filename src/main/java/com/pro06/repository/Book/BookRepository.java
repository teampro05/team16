package com.pro06.repository.Book;

import com.pro06.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // 교재 전체 목록
    @Query("select b from Book b")
    List<Book> bookList1(Integer bno);

    // 해당 교재 파일 정보
    @Query("select b from Book b where b.no = :bno")
    Book bookList2(@Param("bno") Integer bno);
}
