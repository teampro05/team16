package com.pro06.service.Book;

import com.pro06.dto.TbookVO;
import com.pro06.entity.Tbook;
import com.pro06.entity.TbookImg;

import java.util.List;

public interface TBookService {

    public List<Tbook> TbookList(); // 토익 교재 리스트
    public Tbook getTbook(Integer no); // 토익 교재 상세
    public int insertTbook(Tbook tbook); // 토익 교재 추가
    public int updateTbook(Tbook tbook); // 토익 교재 수정
    public int deleteTbook(Tbook tbook); // 토익 교재 삭제

    /////////////////////////////////////////////

    public void insertFileboard(TbookVO fileboard) throws Exception;
    public List<TbookImg> getFileGroupList(int postNo) throws Exception;
    public int removeFileboard(int postNo) throws Exception;
    public void updateFileboard(TbookVO fileboard) throws Exception;
    public void removeFileAll(int postNO) throws Exception;
    public TbookImg thmbn(int no) throws Exception;

}
