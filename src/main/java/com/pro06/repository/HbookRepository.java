package com.pro06.repository;

import com.pro06.entity.Hbook;
import com.pro06.entity.HbookImg;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HbookRepository {

    List<Hbook> HbookList(); // 수능 교재 리스트
    Hbook getHbook(Integer no); // 수능 교재 상세
    int insertHbook(Hbook hbook); // 수능 교재 추가
    int updateHbook(Hbook hbook); // 수능 교재 수정
    int deleteHbook(Hbook hbook); // 수능 교재 삭제

    /////////////////////////////////////////////

    Integer fileBoardInsert(Hbook fileboard) throws Exception;
    Integer fileInsert(HbookImg file) throws Exception;
    List<HbookImg> getFileGroupList(int postNo) throws Exception;
    int fileBoardDelete(int no) throws Exception;
    void fileBoardUpdate(Hbook hbook) throws Exception;
    void fileUpdate(HbookImg hbookImg) throws Exception;
    void removeFileAll(int postNO) throws Exception;
    HbookImg thmbn(int no) throws Exception;
    Hbook latestFileboard() throws Exception;

}
