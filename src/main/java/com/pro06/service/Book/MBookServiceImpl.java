package com.pro06.service.Book;

import com.pro06.dto.MbookVO;
import com.pro06.entity.Mbook;
import com.pro06.entity.MbookImg;
import com.pro06.repository.MbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MBookServiceImpl implements MBookService {

    @Autowired
    private MbookRepository mbookRepository;

    @Override
    public List<Mbook> MbookList() {
        return mbookRepository.MbookList();
    }

    @Override
    public Mbook getMbook(Integer no) {
        return mbookRepository.getMbook(no);
    }

    @Override
    public int insertMbook(Mbook mbook) {
        return mbookRepository.insertMbook(mbook);
    }

    @Override
    public int updateMbook(Mbook mbook) {
        return mbookRepository.updateMbook(mbook);
    }

    @Override
    public int deleteMbook(Mbook mbook) {
        return mbookRepository.deleteMbook(mbook);
    }

    @Override
    public void insertFileboard(MbookVO fileboard) throws Exception {
        Mbook mbook = fileboard.getFileBoard();
        List<MbookImg> fileList = fileboard.getFileList();
        mbookRepository.fileBoardInsert(mbook);
        Mbook latestBoard = mbookRepository.latestFileboard();
        for (MbookImg file:fileList) {
            file.setMbno(latestBoard.getNo());
            mbookRepository.fileInsert(file);
        }
    }

    @Override
    public List<MbookImg> getFileGroupList(int postNo) throws Exception {
        return mbookRepository.getFileGroupList(postNo);
    }

    @Override
    public int removeFileboard(int postNo) throws Exception {
        int ck = mbookRepository.fileBoardDelete(postNo);
        mbookRepository.removeFileAll(postNo);
        return ck;
    }

    @Override
    public void updateFileboard(MbookVO fileboard) throws Exception {
        Mbook mbook = fileboard.getFileBoard();
        List<MbookImg> fileList = fileboard.getFileList();
        mbookRepository.fileBoardUpdate(mbook);
        for(MbookImg file:fileList){
            mbookRepository.fileUpdate(file);
        }
    }

    @Override
    public void removeFileAll(int postNO) throws Exception {
        mbookRepository.removeFileAll(postNO);
    }

    @Override
    public MbookImg thmbn(int no) throws Exception {
        return mbookRepository.thmbn(no);
    }
}
