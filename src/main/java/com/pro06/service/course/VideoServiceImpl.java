package com.pro06.service.course;

import com.pro06.repository.course.VideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class VideoServiceImpl {
    @Autowired
    private VideoRepository videoRepository;
    
    // 강좌 영상
    public List<String> videoList(Integer cno, Integer lno) {
        return videoRepository.videoList(cno, lno);
    }
}
