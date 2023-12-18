package com.pro06.repository;

import com.pro06.entity.Tbook;
import com.pro06.entity.TbookImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TbookRepository {

    List<Tbook> TbookList(); // 토익 교재 리스트
    Tbook getTbook(Integer no); // 토익 교재 상세
    int insertTbook(Tbook tbook); // 토익 교재 추가
    int updateTbook(Tbook tbook); // 토익 교재 수정
    int deleteTbook(Tbook tbook); // 토익 교재 삭제

    /////////////////////////////////////////////

    Integer fileBoardInsert(Tbook fileboard) throws Exception;
    Integer fileInsert(TbookImg file) throws Exception;
    List<TbookImg> getFileGroupList(int postNo) throws Exception;
    int fileBoardDelete(int no) throws Exception;
    void fileBoardUpdate(Tbook tbook) throws Exception;
    void fileUpdate(TbookImg tbookImg) throws Exception;
    void removeFileAll(int postNO) throws Exception;
    TbookImg thmbn(int no) throws Exception;
    Tbook latestFileboard() throws Exception;

}
