package com.pro06.service.course;

import com.pro06.dto.course.LecQueDto;
import com.pro06.dto.course.VideoDto;
import com.pro06.entity.course.Video;
import com.pro06.repository.course.VideoRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class VideoServiceImpl {
    @Autowired
    private VideoRepository videoRepository;

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
}
