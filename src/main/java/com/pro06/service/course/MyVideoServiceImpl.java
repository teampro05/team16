package com.pro06.service.course;

import com.pro06.dto.MyVideoDto;
import com.pro06.entity.MyVideo;
import com.pro06.repository.course.MyVideoRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class MyVideoServiceImpl {

    @Autowired
    private MyVideoRepository myVideoRepository;

    @Autowired
    private ModelMapper modelMapper;

    // 내가본 강의 영상 정보 확인
//    public List<MyCourse> myVideoList(String id, Integer cno, Integer lno) { return myVideoRepository.myVideoList(id, cno, lno); }

    // 내 강의 영상 정보 등록
    public void myVideoInsert(MyVideoDto myVideoDto) {
        MyVideo myVideo = modelMapper.map(myVideoDto, MyVideo.class);
        myVideoRepository.save(myVideo);
    }
    
    // 해당 강의 영상정보가 있는지 확인
    public Integer getMyVideoCnt(String id, Integer cno, Integer lno) { return myVideoRepository.getMyVideoCnt(id, cno, lno); }

    // 해당 강의 영상정보가 있는지 확인
    public MyVideoDto getMyVideo(String id, Integer cno, Integer lno) {
        Optional<MyVideo> myVideo = myVideoRepository.getMyVideo(id, cno, lno);
        if(myVideo.isPresent()) {   // Optional 에서 null 비교할 때 사용
            return modelMapper.map(myVideo, MyVideoDto.class);
        } else {
            return null;
        }
    }
    
    // page, sec, state 업데이트
    // 강의 위치, 시간, 완료여부 업데이트
    public void updateSecPageState(MyVideoDto myVideoDto) {
        Optional<MyVideo> myVideo = myVideoRepository.getMyVideo(myVideoDto.getId(),
                myVideoDto.getCourse().getNo(), myVideoDto.getLecture().getNo());
        MyVideo res = myVideo.orElseThrow();
        res.secPageState(myVideoDto.getSec(), myVideoDto.getPage(), myVideoDto.getState());
        myVideoRepository.save(res);
    }

    // page, sec 업데이트
    // 강의 위치, 시간 업데이트
    public void updateSecPage(MyVideoDto myVideoDto) {
        Optional<MyVideo> myVideo = myVideoRepository.getMyVideo(myVideoDto.getId(),
                myVideoDto.getCourse().getNo(), myVideoDto.getLecture().getNo());
        MyVideo res = myVideo.orElseThrow();
        res.secPage(myVideoDto.getSec(), myVideoDto.getPage());
        myVideoRepository.save(res);
    }
}
