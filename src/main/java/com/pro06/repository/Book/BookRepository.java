package com.pro06.repository.Book;

import com.pro06.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    // Optional<Book> findAllBy();
    // Optional<Book> findBookById(@Param("bno") Integer bno);

    // 교재 목록 - 전체
    @Query("select b from Book b")
    List<Book> bookList();

    // 교재 상세
    @Query("select b from Book b where b.no = :bno")
    Book getBook(@Param("bno") Integer bno);

}
