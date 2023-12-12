package com.pro06.service.course;

import com.pro06.dto.*;
import com.pro06.entity.*;
import com.pro06.repository.course.*;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Log4j2
public class LectureServiceImpl {
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private LecTestRepository lecTestRepository;

    @Autowired
    private LecAnsRepository lecAnsRepository;

    @Autowired
    private ModelMapper modelMapper;

    // 강의 정보 + 파일 등록
    public void LectureVoInsert(LectureVO vo) throws Exception {
        LectureDto lecture = vo.getLecture();
        List<VideoDto> fileList = vo.getFileList();
        
        // 강의 정보 등록
        Optional<Course> cou = courseRepository.findById(vo.getCno());
        CourseDto coudto = modelMapper.map(cou, CourseDto.class);
        lecture.setCourse(coudto);
        Lecture lec = modelMapper.map(lecture, Lecture.class);
        
        // 저장 및 dto로 변환
        Lecture lec2 = lectureRepository.save(lec);
        // 이거 안해주면 오류뜸
        LectureDto lecDto = modelMapper.map(lec2, LectureDto.class);

        log.info("lecture 저장");
        
        // 강의 영상 등록
        for(VideoDto video: fileList) {
            video.setLecture(lecDto);
            video.setCourse(coudto);
            Video vdo = modelMapper.map(video, Video.class);
            videoRepository.save(vdo);
            log.info("video 저장");
        }
        
        // 시험 정보 등록
        LecTestDto lecTest = vo.getLecTest();
        lecTest.setLecture(lecDto);
        lecTest.setCourse(coudto);
        LecTest lt = modelMapper.map(lecTest, LecTest.class);
        lecTestRepository.save(lt);
    }

    // 해당강의의 강좌 목록 불러오기
    public List<LectureDto> lectureCnoList(Integer cno) {
        List<Lecture> lst = lectureRepository.lectureCnoList(cno);
        List<LectureDto> lectureDtoList = lst.stream().map(lecture ->
                modelMapper.map(lecture, LectureDto.class))
                .collect(Collectors.toList());
        return lectureDtoList;
    }
    
    // 시험 정보 가져오기
    public LecTestDto getLecTest(Integer cno, Integer lno) {
        LecTest opt = lecTestRepository.getLecTest(cno, lno);
        LecTestDto dto = modelMapper.map(opt, LecTestDto.class);
        return dto;
    }
    
    // 시험 제출 답안 입력, 수정
    public void lecAnsInsUpd(LecAnsDto lecAnsDto) {
        LecAns lecAns = modelMapper.map(lecAnsDto, LecAns.class);
        lecAnsRepository.save(lecAns);
//        lecAnsRepository.findById();
    }
    
    // 제출된 답안 수정
    public LecAns lecAnsUpdate(LecAns lecAns) {return lecAnsRepository.save(lecAns);}
    
    // 답안 정보 추출
    public LecAnsDto getLecAns(Integer cno, Integer lno, String id) {
        LecAns lecAns = lecAnsRepository.getLecAns(cno, lno, id);
        LecAnsDto dto = modelMapper.map(lecAns, LecAnsDto.class);
        return dto;
    }
}
