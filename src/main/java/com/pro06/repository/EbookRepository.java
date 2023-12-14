package com.pro06.repository;

import com.pro06.entity.Ebook;
import com.pro06.entity.EbookImg;
import org.apache.ibatis.annotations.Mapper;

import java.io.File;
import java.util.List;

@Mapper
public interface EbookRepository {

    List<Ebook> EbookList(); // 초등 교재 리스트
    Ebook getEbook(Integer no); // 초등 교재 상세
    int insertEbook(Ebook ebook); // 초등 교재 추가
    int updateEbook(Ebook ebook); // 초등 교재 수정
    int deleteEbook(Ebook ebook); // 초등 교재 삭제

    /////////////////////////////////////////////

    Integer fileBoardInsert(Ebook fileboard) throws Exception;
    Integer fileInsert(EbookImg file) throws Exception;
    List<EbookImg> getFileGroupList(int postNo) throws Exception;
    int fileBoardDelete(int no) throws Exception;
    void fileBoardUpdate(Ebook ebook) throws Exception;
    void fileUpdate(EbookImg ebookImg) throws Exception;
    void removeFileAll(int postNO) throws Exception;
    EbookImg thmbn(int no) throws Exception;
    Ebook latestFileboard() throws Exception;

}
