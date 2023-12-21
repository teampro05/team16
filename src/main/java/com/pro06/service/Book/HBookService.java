package com.pro06.service.Book;

import com.pro06.dto.HbookVO;
import com.pro06.entity.Ebook;
import com.pro06.entity.Hbook;
import com.pro06.entity.HbookImg;

import java.util.List;

public interface HBookService {

    public List<Hbook> HbookList(); // 수능 교재 리스트
    List<Hbook> admHbookList(); // 초등 교재 리스트, 관리자 용
    int hbookDelRec(Hbook hbook);  // 초등 교재 삭제, 복구 기능
    public Hbook getHbook(Integer no); // 수능 교재 상세
    public int insertHbook(Hbook hbook); // 수능 교재 추가
    public int updateHbook(Hbook hbook); // 수능 교재 수정
    public int deleteHbook(Hbook hbook); // 수능 교재 삭제

    /////////////////////////////////////////////

    public void insertFileboard(HbookVO fileboard) throws Exception;
    public List<HbookImg> getFileGroupList(int postNo) throws Exception;
    public int removeFileboard(int postNo) throws Exception;
    public void updateFileboard(HbookVO fileboard) throws Exception;
    public void removeFileAll(int postNO) throws Exception;
    public HbookImg thmbn(int no) throws Exception;

}
