package com.pro06.service;

import com.pro06.entity.Notice;
import com.pro06.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
public class NoticeServiceImpl implements NoticeService{

    @Autowired
    private NoticeRepository noticeRepository;

    @Override
    public void NoticeDelete(Integer no) {
        noticeRepository.deleteById(no);
    }

    @Override
    public List<Notice> NoticeList() {
        return noticeRepository.findAll();
    }

    @Override
    public Notice NoticeInsert(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public Notice NoticeUpdate(Notice notice) {
        return noticeRepository.save(notice);
    }
}
