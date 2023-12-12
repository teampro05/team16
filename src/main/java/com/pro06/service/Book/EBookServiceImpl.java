package com.pro06.service.Book;

import com.pro06.dto.EbookVO;
import com.pro06.entity.Ebook;
import com.pro06.entity.EbookImg;
import com.pro06.repository.Book.EbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EBookServiceImpl implements EBookService {

    @Autowired
    private EbookRepository ebookRepository;

    @Override
    public List<Ebook> EbookList() {
        return ebookRepository.EbookList();
    }

    @Override
    public Ebook getEbook(Integer no) {
        return ebookRepository.getEbook(no);
    }

    @Override
    public int insertEbook(Ebook ebook) {
        return ebookRepository.insertEbook(ebook);
    }

    @Override
    public int updateEbook(Ebook ebook) {
        return ebookRepository.updateEbook(ebook);
    }

    @Override
    public int deleteEbook(Ebook ebook) {
        return ebookRepository.deleteEbook(ebook);
    }

    @Override
    public void insertFileboard(EbookVO fileboard) throws Exception {
        Ebook ebook = fileboard.getFileBoard();
        List<EbookImg> fileList = fileboard.getFileList();
        ebookRepository.fileBoardInsert(ebook);
    }

    @Override
    public List<EbookImg> getFileGroupList(int postNo) throws Exception {
        return ebookRepository.getFileGroupList(postNo);
    }

    @Override
    public int removeFileboard(int postNo) throws Exception {
        int ck = ebookRepository.fileBoardDelete(postNo);
        ebookRepository.removeFileAll(postNo);
        return ck;
    }

    @Override
    public void updateFileboard(EbookVO fileboard) throws Exception {
        Ebook board = fileboard.getFileBoard();
        List<EbookImg> fileList = fileboard.getFileList();
        ebookRepository.fileBoardUpdate(board);
        for(EbookImg file:fileList){
            ebookRepository.fileUpdate(file);
        }
    }

    @Override
    public void removeFileAll(int postNO) throws Exception {
        ebookRepository.removeFileAll(postNO);
    }

    @Override
    public EbookImg thmbn(int no) throws Exception {
        return ebookRepository.thmbn(no);
    }
}
