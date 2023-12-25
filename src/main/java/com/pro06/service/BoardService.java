package com.pro06.service;

import com.pro06.dto.BoardDTO;
import com.pro06.entity.Faq;

import java.util.List;

public interface BoardService {

    // Notice
    public void NoticeInsert(BoardDTO boardDTO);
    public void NoticeUpdate(BoardDTO boardDTO);
    public void NoticeDelete(Integer no);
    public List<BoardDTO> NoticeList();
    public BoardDTO NoticeGet(Integer no);


    // FAQ
    public void faqInsert(BoardDTO boardDTO);
    public void faqUpdate(BoardDTO boardDTO);
    public void faqDelete(Integer no);
    public List<BoardDTO> faqList();
    public BoardDTO faqGet(Integer no);

    //Oto

    public void otoInsert(BoardDTO boardDTO);
    public void otoDelete(Integer no);
    public List<BoardDTO> otoList();
    public BoardDTO otoGet(Integer no);



}
