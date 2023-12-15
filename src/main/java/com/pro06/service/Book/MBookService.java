package com.pro06.service.Book;

import com.pro06.dto.MbookVO;
import com.pro06.entity.Mbook;
import com.pro06.entity.MbookImg;

import java.util.List;

public interface MBookService {

    public List<Mbook> MbookList(); // 중학 교재 리스트
    public Mbook getMbook(Integer no); // 중학 교재 상세
    public int insertMbook(Mbook mbook); // 중학 교재 추가
    public int updateMbook(Mbook mbook); // 중학 교재 수정
    public int deleteMbook(Mbook mbook); // 중학 교재 삭제

    /////////////////////////////////////////////

    public void insertFileboard(MbookVO fileboard) throws Exception;
//    public Integer fileInsert(EbookImg file) throws Exception;
    public List<MbookImg> getFileGroupList(int postNo) throws Exception;
    public int removeFileboard(int postNo) throws Exception;
    public void updateFileboard(MbookVO mbook) throws Exception;
//    public void fileUpdate(EbookImg ebookImg) throws Exception;
    public void removeFileAll(int postNO) throws Exception;
    public MbookImg thmbn(int no) throws Exception;

}
