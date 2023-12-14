package com.pro06.service.course;

import com.pro06.entity.MyVideo;
import com.pro06.repository.course.MyVideoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class MyVideoServiceImpl {

    @Autowired
    private MyVideoRepository myVideoRepository;

    // 내가본 강의 영상 정보 확인
//    public List<MyCourse> myVideoList(String id, Integer cno, Integer lno) { return myVideoRepository.myVideoList(id, cno, lno); }

    // 내 강의 영상 정보 등록
    public MyVideo myVideoInsert(MyVideo myVideo) {
        return myVideoRepository.save(myVideo);
    }
    
    // 해당 강의 영상정보가 있는지 확인
    public Integer getMyVideoCnt(String id, Integer cno, Integer lno) { return myVideoRepository.getMyVideoCnt(id, cno, lno); }

    // 해당 강의 영상정보가 있는지 확인
    public MyVideo getMyVideo(String id, Integer cno, Integer lno) { return myVideoRepository.getMyVideo(id, cno, lno); }
    
    // page와 sec 업데이트
    public void updatePageSec(MyVideo myVideo) { myVideoRepository.updatePageSec(myVideo);}
}
