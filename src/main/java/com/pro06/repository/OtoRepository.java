package com.pro06.repository;

import com.pro06.entity.Faq;
import com.pro06.entity.Oto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OtoRepository extends JpaRepository<Oto, Integer> {

}
