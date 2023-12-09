package com.pro06.service;

import com.pro06.entity.Notice;

import java.util.List;

public interface NoticeService {
    public Notice NoticeInsert(Notice notice);
    public Notice NoticeUpdate(Notice notice);
    public void NoticeDelete(Integer no);
    public List<Notice> NoticeList();

}
