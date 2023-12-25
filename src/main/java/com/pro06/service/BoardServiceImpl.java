package com.pro06.service;

import com.pro06.dto.BoardDTO;
import com.pro06.entity.Faq;
import com.pro06.entity.Notice;
import com.pro06.entity.Oto;
import com.pro06.repository.FaqRepository;
import com.pro06.repository.NoticeRepository;
import com.pro06.repository.OtoRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BoardServiceImpl implements BoardService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private FaqRepository faqRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OtoRepository otoRepository;
    //  Notice

    @Override
    public void NoticeDelete(Integer no) {
        noticeRepository.deleteById(no);
    }

    @Override
    public List<BoardDTO> NoticeList() {
        List<Notice> noticeList = noticeRepository.findAll();
        List<BoardDTO> boardDTOList = noticeList.stream().map(notice ->
                        modelMapper.map(notice, BoardDTO.class))
                .collect(Collectors.toList());
        return boardDTOList;
    }

    @Override
    public void NoticeInsert(BoardDTO boardDTO) {
        Notice notice = modelMapper.map(boardDTO, Notice.class);
        noticeRepository.save(notice);
    }

    @Override
    public void NoticeUpdate(BoardDTO boardDTO) {
        Notice notice = modelMapper.map(boardDTO, Notice.class);
        noticeRepository.save(notice);
    }

    @Override
    public BoardDTO NoticeGet(Integer no) {
        Optional<Notice> result = noticeRepository.findById(no);
        Notice notice = result.orElseThrow();
        BoardDTO boardDTO = modelMapper.map(notice, BoardDTO.class);
        return boardDTO; }

    //  Faq


    @Override
    public void faqInsert(BoardDTO boardDTO) {
        Faq faq = modelMapper.map(boardDTO, Faq.class);
        faqRepository.save(faq);
    }

    @Override
    public void faqUpdate(BoardDTO boardDTO) {
        Faq faq = modelMapper.map(boardDTO, Faq.class);
        faqRepository.save(faq);
    }

    @Override
    public void faqDelete(Integer no) {
        faqRepository.deleteById(no);
    }

    @Override
    public List<BoardDTO> faqList() {
        List<Faq> faqList = faqRepository.findAll();
        List<BoardDTO> boardDTOList = faqList.stream().map(faq ->
                        modelMapper.map(faq, BoardDTO.class))
                .collect(Collectors.toList());
        return boardDTOList;
    }

    @Override
    public BoardDTO faqGet(Integer no) {
        Optional<Faq> result = faqRepository.findById(no);
        Faq faq = result.orElseThrow();
        BoardDTO boardDTO = modelMapper.map(faq, BoardDTO.class);
        return boardDTO;
    }

    //  Oto

    @Override
    public void otoInsert(BoardDTO boardDTO) {
        Oto oto = modelMapper.map(boardDTO, Oto.class);
        otoRepository.save(oto);
    }

    @Override
    public void otoDelete(Integer no) {
        otoRepository.deleteById(no);
    }

    @Override
    public List<BoardDTO> otoList() {
        List<Oto> otoList = otoRepository.findAll();
        List<BoardDTO> boardDTOList = otoList.stream().map(oto ->
                        modelMapper.map(oto, BoardDTO.class))
                .collect(Collectors.toList());
        return boardDTOList;
    }

    @Override
    public BoardDTO otoGet(Integer no) {
        Optional<Oto> result = otoRepository.findById(no);
        Oto oto = result.orElseThrow();
        BoardDTO boardDTO = modelMapper.map(oto, BoardDTO.class);
        return boardDTO;
    }
}
