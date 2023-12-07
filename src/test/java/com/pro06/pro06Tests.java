package com.pro06;

import com.pro06.entity.Role;
import com.pro06.entity.User;
import com.pro06.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


@SpringBootTest
public class pro06Tests {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserRepository userRepository;
//
//    @Test
//    public void Userinsert(){
//            User user = User.builder()
//                    .id("adminㅡㅡㅡㅡ1112131311")
//                    .pw("1234")
//                    .name("name")
//                    .build();
//            userRepository.save(user);
//    }

//
//    @Test
//    public void userget(){
//        User user = userRepository.getId1("admin");
//        logger.info("11ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ" + user);
//        System.out.println(user);
//    }



//        @Test
//        public void testInsert () {
//            IntStream.rangeClosed(1, 100).forEach(i -> {
//                User user = User.builder()
//                        .id("id")
//                        .pw("pw")
//                        .name("name")
//                        .tel("tel")
//                        .email("email")
//                        .addr1("addr1")
//                        .addr2("addr2")
//                        .postcode("postcode")
//                        .build();
//                userRepository.save(user);
//                 INSERT INTO board (title, content, user) VALUES ('title1', 'content1', 'user0');
//             log.info(user);
//            });
//        }
//
//    @Test
//    public void testSelect() {
//        Long bno = 100L;
//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        // SELECT * FROM board WHERE bno = 100;
//        log.info(board);
//    }
//
//    @Test
//    public void testUpdate() {
//        Long bno = 100L;
//        Optional<Board> result = boardRepository.findById(bno);
//        Board board = result.orElseThrow();
//        // SELECT * FROM board WHERE bno = 100;
//        board.setTitle("update title 100");
//        board.setContent("update content 100");
//        // bno 100 번의 title, content만 변경
//        boardRepository.save(board);
//        // UPDATE board SET title='update title 100', content='update content 100' WHERE bno = 100;
//    }
//
//    @Test
//    public void testDelete() {
//        Long bno = 1L;
//        boardRepository.deleteById(bno);
//        // DELETE FROM board WHERE bno = 1;
//    }
//
//    @Test
//    public void testPaging() {
//        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
//        Page<Board> result = boardRepository.findAll(pageable);
//        // SELECT * FROM board ORDER BY bno DESC LIMIT 0, 10;
//
//        log.info("total count: "+result.getTotalElements());
//        log.info( "total pages:" +result.getTotalPages());
//        log.info("page number: "+result.getNumber());
//        log.info("page size: "+result.getSize());
//
//        List<Board> boardList = result.getContent();
//
//        boardList.forEach(board -> log.info(board));
//    }
//}
    }
