package com.pro06.service.Book;

import com.pro06.dto.EbookVO;
import com.pro06.entity.Ebook;
import com.pro06.entity.EbookImg;

import java.util.List;

public interface EBookService {

    public List<Ebook> EbookList(); // 초등 교재 리스트
    public Ebook getEbook(Integer no); // 초등 교재 상세
    public int insertEbook(Ebook ebook); // 초등 교재 추가
    public int updateEbook(Ebook ebook); // 초등 교재 수정
    public int deleteEbook(Ebook ebook); // 초등 교재 삭제

    /////////////////////////////////////////////

    public void insertFileboard(EbookVO fileboard) throws Exception;
//    public Integer fileInsert(EbookImg file) throws Exception;
    public List<EbookImg> getFileGroupList(int postNo) throws Exception;
    public int removeFileboard(int postNo) throws Exception;
    public void updateFileboard(EbookVO ebook) throws Exception;
//    public void fileUpdate(EbookImg ebookImg) throws Exception;
    public void removeFileAll(int postNO) throws Exception;
    public EbookImg thmbn(int no) throws Exception;

}
