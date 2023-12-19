package com.pro06.service.course;

import com.pro06.dto.course.*;
import com.pro06.entity.course.*;
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
    private LecQueRepository lecQueRepository;

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

    // 강의, 파일, 시험 정보 가져오기
    public LectureVO getLectureVo(Integer lno, Integer cno) throws Exception {
        // 강의 정보 추출
        Optional<Lecture> lec = lectureRepository.findById(lno);
        LectureDto lecDto = modelMapper.map(lec, LectureDto.class);
        
        // 영상 정보 추출
        List<Video> vilst = videoRepository.videoList(cno, lno);
        List<VideoDto> videoDtoList = vilst.stream().map(video ->
                modelMapper.map(video, VideoDto.class))
                .collect(Collectors.toList());
        
        // 시험 정보 추출
        LecTest test = lecTestRepository.getLecTest(cno, lno);
        LecTestDto testDto = modelMapper.map(test, LecTestDto.class);

        LectureVO lectureVO = new LectureVO();
        lectureVO.setLecture(lecDto);
        lectureVO.setLecTest(testDto);
        lectureVO.setFileList(videoDtoList);
        return lectureVO;
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
        if(opt != null) {
            return modelMapper.map(opt, LecTestDto.class);
        } else {
            return null;
        }
    }


    // region lecAns
    // 시험 제출 답안 입력, 수정
    public void lecAnsInsert(LecAnsDto lecAnsDto) {
        LecAns lecAns = modelMapper.map(lecAnsDto, LecAns.class);
        lecAnsRepository.save(lecAns);
    }
    
    // 제출된 답안 수정
    public void lecAnsUpdate(LecAnsDto lecAnsDto) {
        Optional<LecAns> lec = lecAnsRepository.getLecAns(lecAnsDto.getCourse().getNo(),
                lecAnsDto.getLecture().getNo(), lecAnsDto.getId());
        LecAns lecAns = lec.orElseThrow();
        lecAns.answerChange(lecAnsDto.getAnswer1(), lecAnsDto.getAnswer2(), lecAnsDto.getAnswer3(),
                lecAnsDto.getAnswer4(), lecAnsDto.getAnswer5(), lecAnsDto.getAnsCnt());
        lecAnsRepository.save(lecAns);
    }
    
    // 답안 정보 추출
    public LecAnsDto getLecAns(Integer cno, Integer lno, String id) {
        Optional<LecAns> lecAns = lecAnsRepository.getLecAns(cno, lno, id);
        log.warn("lecAns.isPresent() : " + lecAns.isPresent());
        if(lecAns.isPresent()) { // Optional 에서 null 비교할 때 사용
            LecAnsDto lecAnsDto = modelMapper.map(lecAns, LecAnsDto.class);
            return lecAnsDto;
        } else {
            return null;
        }
    }
    // endregion


    // region lecQue
    // admin 용
    // admin 에서 사용할 list
    // 모든 질문 목록
    public List<LecQueDto> lecQueDtoFindByAll() {
        List<LecQue> lst = lecQueRepository.findAll();
        List<LecQueDto> dtoList = lst.stream().map(lecQue ->
                        modelMapper.map(lecQue, LecQueDto.class))
                .collect(Collectors.toList());
        return dtoList;
    }
    
    // user 용
    // video 에서 사용할 list
    // 해당 강좌, 강의, 영상에서 유저가 질문한 목록
    public List<LecQueDto> lecQueList(LecQueDto lecQueDto) {
        List<LecQue> lst = lecQueRepository.lecQueList(lecQueDto.getId(), lecQueDto.getPage(),
                lecQueDto.getCourse().getNo(), lecQueDto.getLecture().getNo(), "n");
        List<LecQueDto> dtoList = lst.stream().map(lecQue ->
                        modelMapper.map(lecQue, LecQueDto.class))
                .collect(Collectors.toList());
        return dtoList;
    }

    // 질문 입력
    public LecQueDto lecQueInsert(LecQueDto lecQueDto) {
        LecQue lecQue = modelMapper.map(lecQueDto, LecQue.class);
        LecQue lec = lecQueRepository.save(lecQue);
        LecQueDto dto = modelMapper.map(lec, LecQueDto.class);
        return dto;
    }

    // 질문 보기
    public LecQueDto getLecQue(Integer no) {
        Optional<LecQue> lecQue = lecQueRepository.findById(no);
        LecQueDto dto = modelMapper.map(lecQue, LecQueDto.class);
        return dto;
    }

    // 질문 삭제
    public void lecQueDelete(LecQueDto dto) {
        Optional<LecQue> lecQue = lecQueRepository.findById(dto.getNo());
        LecQue lecQue1 = lecQue.orElseThrow();
        lecQue1.delete(dto.getDeleteYn());
        lecQueRepository.save(lecQue1);
    }

    // 질문에 대한 답변 입력, 수정
    public void lecQueAnsInsUpd(LecQueDto lecQueDto) {
        Optional<LecQue> lecQue = lecQueRepository.findById(lecQueDto.getNo());
        LecQue lecQue1 = lecQue.orElseThrow();
        lecQue1.answer(lecQueDto.getAns());
        lecQueRepository.save(lecQue1);
    }

    // 질문 삭제 취소
    public void lecQueAnsRecover(Integer no) {
        Optional<LecQue> lecQue = lecQueRepository.findById(no);
        LecQue lecQue1 = lecQue.orElseThrow();
        lecQue1.delete("n");
        lecQueRepository.save(lecQue1);
    }
    // endregion
}
