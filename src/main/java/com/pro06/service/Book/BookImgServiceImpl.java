package com.pro06.service.Book;

import com.pro06.entity.BookImg;
import com.pro06.repository.Book.BookImgRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@Service
public class BookImgServiceImpl implements BookImgService{

    @Autowired
    private BookImgRepository bookImgRepository;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    @Override
    public List<BookImg> bookImgList(Integer bno) {
        return bookImgRepository.bookImgList(bno);
    }

    @Override
    public BookImg getBookImg(Integer no) {
        return bookImgRepository.getBookImg(no);
    }

    @Override
    public void insertBookImg(BookImg bookImg) {
        bookImgRepository.save(bookImg);
    }

    @Override
    public void deleteBookImg(BookImg bookImg) {
        bookImgRepository.delete(bookImg);
    }

//    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception{
//        String oriImgName = itemImgFile.getOriginalFilename();
//        String imgName = "";
//        String imgUrl = "";
//
//        //파일 업로드
//        if(!StringUtils.isEmpty(oriImgName)){
//            imgName = fileService.uploadFile(itemImgLocation, oriImgName,
//                    itemImgFile.getBytes());
//            imgUrl = "/images/item/" + imgName;
//        }
//
//        //상품 이미지 정보 저장
//        itemImg.updateItemImg(oriImgName, imgName, imgUrl);
//        itemImgRepository.save(itemImg);
//    }
//
//    public void updateItemImg(Long itemImgId, MultipartFile itemImgFile) throws Exception{
//        if(!itemImgFile.isEmpty()){
//            ItemImg savedItemImg = itemImgRepository.findById(itemImgId)
//                    .orElseThrow(EntityNotFoundException::new);
//
//            //기존 이미지 파일 삭제
//            if(!StringUtils.isEmpty(savedItemImg.getImgName())) {
//                fileService.deleteFile(itemImgLocation+"/"+
//                        savedItemImg.getImgName());
//            }
//
//            String oriImgName = itemImgFile.getOriginalFilename();
//            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
//            String imgUrl = "/images/item/" + imgName;
//            savedItemImg.updateItemImg(oriImgName, imgName, imgUrl);
//        }
//    }
}
