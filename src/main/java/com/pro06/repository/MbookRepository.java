package com.pro06.repository;

import com.pro06.entity.Ebook;
import com.pro06.entity.Mbook;
import com.pro06.entity.MbookImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MbookRepository {

    List<Mbook> MbookList(); // 중학 교재 리스트
    List<Mbook> admMbookList(); // 초등 교재 리스트, 관리자 용
    int mbookDelRec(Mbook mbook);  // 초등 교재 삭제, 복구 기능
    Mbook getMbook(Integer no); // 중학 교재 상세
    int insertMbook(Mbook mbook); // 중학 교재 추가
    int updateMbook(Mbook mbook); // 중학 교재 수정
    int deleteMbook(Mbook mbook); // 중학 교재 삭제

    /////////////////////////////////////////////

    Integer fileBoardInsert(Mbook fileboard) throws Exception;
    Integer fileInsert(MbookImg file) throws Exception;
    List<MbookImg> getFileGroupList(int postNo) throws Exception;
    int fileBoardDelete(int no) throws Exception;
    void fileBoardUpdate(Mbook mbook) throws Exception;
    void fileUpdate(MbookImg mbookImg) throws Exception;
    void removeFileAll(int postNO) throws Exception;
    MbookImg thmbn(int no) throws Exception;
    Mbook latestFileboard() throws Exception;

}
