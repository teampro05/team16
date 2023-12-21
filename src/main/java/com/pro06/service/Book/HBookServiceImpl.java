package com.pro06.service.Book;


import com.pro06.dto.HbookVO;
import com.pro06.entity.*;
import com.pro06.repository.HbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HBookServiceImpl implements HBookService {

    @Autowired
    private HbookRepository hbookRepository;

    @Override
    public List<Hbook> admHbookList() {
        return hbookRepository.admHbookList();
    }

    @Override
    public int hbookDelRec(Hbook hbook) {
        return hbookRepository.hbookDelRec(hbook);
    }

    @Override
    public List<Hbook> HbookList() {
        return hbookRepository.HbookList();
    }

    @Override
    public Hbook getHbook(Integer no) {
        return hbookRepository.getHbook(no);
    }

    @Override
    public int insertHbook(Hbook hbook) {
        return hbookRepository.insertHbook(hbook);
    }

    @Override
    public int updateHbook(Hbook hbook) {
        return hbookRepository.updateHbook(hbook);
    }

    @Override
    public int deleteHbook(Hbook hbook) {
        return hbookRepository.deleteHbook(hbook);
    }

    @Override
    public void insertFileboard(HbookVO fileboard) throws Exception {
        Hbook hbook = fileboard.getFileBoard();
        List<HbookImg> fileList = fileboard.getFileList();
        hbookRepository.fileBoardInsert(hbook);
        Hbook latestBoard = hbookRepository.latestFileboard();
        for (HbookImg file:fileList) {
            file.setHbno(latestBoard.getNo());
            hbookRepository.fileInsert(file);
        }
    }

    @Override
    public List<HbookImg> getFileGroupList(int postNo) throws Exception {
        return hbookRepository.getFileGroupList(postNo);
    }

    @Override
    public int removeFileboard(int postNo) throws Exception {
        int ck = hbookRepository.fileBoardDelete(postNo);
        hbookRepository.removeFileAll(postNo);
        return ck;
    }

    @Override
    public void updateFileboard(HbookVO fileboard) throws Exception {
        Hbook hbook = fileboard.getFileBoard();
        List<HbookImg> fileList = fileboard.getFileList();
        hbookRepository.fileBoardUpdate(hbook);
        for(HbookImg file:fileList){
            hbookRepository.fileUpdate(file);
        }
    }

    @Override
    public void removeFileAll(int postNO) throws Exception {
        hbookRepository.removeFileAll(postNO);
    }

    @Override
    public HbookImg thmbn(int no) throws Exception {
        return hbookRepository.thmbn(no);
    }
}
