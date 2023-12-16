package com.pro06.service.Book;

import com.pro06.dto.TbookVO;
import com.pro06.entity.Tbook;
import com.pro06.entity.TbookImg;
import com.pro06.repository.TbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TBookServiceImpl implements TBookService {

    @Autowired
    private TbookRepository tbookRepository;

    @Override
    public List<Tbook> TbookList() {
        return tbookRepository.TbookList();
    }

    @Override
    public Tbook getTbook(Integer no) {
        return tbookRepository.getTbook(no);
    }

    @Override
    public int insertTbook(Tbook tbook) {
        return tbookRepository.insertTbook(tbook);
    }

    @Override
    public int updateTbook(Tbook tbook) {
        return tbookRepository.updateTbook(tbook);
    }

    @Override
    public int deleteTbook(Tbook tbook) {
        return tbookRepository.deleteTbook(tbook);
    }

    @Override
    public void insertFileboard(TbookVO fileboard) throws Exception {
        Tbook tbook = fileboard.getFileBoard();
        List<TbookImg> fileList = fileboard.getFileList();
        tbookRepository.fileBoardInsert(tbook);
        Tbook latestBoard = tbookRepository.latestFileboard();
        for (TbookImg file:fileList) {
            file.setTbno(latestBoard.getNo());
            tbookRepository.fileInsert(file);
        }
    }

    @Override
    public List<TbookImg> getFileGroupList(int postNo) throws Exception {
        return tbookRepository.getFileGroupList(postNo);
    }

    @Override
    public int removeFileboard(int postNo) throws Exception {
        int ck = tbookRepository.fileBoardDelete(postNo);
        tbookRepository.removeFileAll(postNo);
        return ck;
    }

    @Override
    public void updateFileboard(TbookVO fileboard) throws Exception {
        Tbook tbook = fileboard.getFileBoard();
        List<TbookImg> fileList = fileboard.getFileList();
        tbookRepository.fileBoardUpdate(tbook);
        for(TbookImg file:fileList){
            tbookRepository.fileUpdate(file);
        }
    }

    @Override
    public void removeFileAll(int postNO) throws Exception {
        tbookRepository.removeFileAll(postNO);
    }

    @Override
    public TbookImg thmbn(int no) throws Exception {
        return tbookRepository.thmbn(no);
    }
}
