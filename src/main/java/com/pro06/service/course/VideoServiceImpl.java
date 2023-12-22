package com.pro06.service.course;

import com.pro06.dto.course.CourseVideoDto;
import com.pro06.dto.course.VideoDto;
import com.pro06.entity.course.CourseVideo;
import com.pro06.entity.course.Video;
import com.pro06.repository.course.CouVdoRepository;
import com.pro06.repository.course.VideoRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class VideoServiceImpl {
    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private CouVdoRepository couVdoRepository;

    @Autowired
    private ModelMapper modelMapper;
    
    // 강좌 영상
    public List<VideoDto> videoList(Integer cno, Integer lno) {
        List<Video> lst = videoRepository.videoList(cno, lno);
        List<VideoDto> dtoList = lst.stream().map(video ->
                modelMapper.map(video, VideoDto.class))
                .collect(Collectors.toList());
        return dtoList;
    }
    
    // 해당 강좌의 ot 영상 추출
    public CourseVideoDto getCourseVideo(Integer cno) {
        Optional<CourseVideo> vdo = couVdoRepository.getCouVideo(cno);
        CourseVideoDto videoDto;
        if(vdo != null) {
            videoDto = modelMapper.map(vdo, CourseVideoDto.class);
        } else {
            videoDto = null;
        }
        return videoDto;
    }

    // 해당 강좌의 ot 영상 저장
    public void couVdoInsert(CourseVideoDto videoDto) {
        CourseVideo couVdo = modelMapper.map(videoDto, CourseVideo.class);
        couVdoRepository.save(couVdo);
    }

    // 해당 강좌의 ot 영상 수정
    public void couVdoUpdate(CourseVideoDto videoDto) {
        Optional<CourseVideo> couVdo = couVdoRepository.getCouVideo(videoDto.getCourse().getNo());
        CourseVideo res = couVdo.orElseThrow();
        res.change(videoDto);
        couVdoRepository.save(res);
    }
}
