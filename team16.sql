CREATE DATABASE team16;
USE team16;

-- 회원
CREATE TABLE user(
                     no INT PRIMARY KEY AUTO_INCREMENT,            -- 고유번호
                     id VARCHAR(20) UNIQUE KEY NOT NULL,             -- 로그인 아이디
                     pw VARCHAR(300) NOT NULL,                       -- 비밀번호
                     name VARCHAR(100) NOT NULL,                     -- 이름
                     tel VARCHAR(20) NOT NULL,                       -- 전화번호
                     email VARCHAR(100),                             -- 이메일
                     addr1 VARCHAR(100),                             -- 주소
                     addr2 VARCHAR(200),                             -- 상세 주소
                     addr3 VARCHAR(100),                             -- 주요 직거래 주소
                     postcode VARCHAR(10),                           -- 우편 번호
                     status ENUM('ACTIVE','DORMANT','REMOVE'),        -- REMOVE(삭제), DORMANT(휴면), ACTIVE(활동)
                     ROLE ENUM('USER','ADMIN','TEACHER'),
                     createAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- 회원 등록일
                     loginAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP     -- 마지막 로그인
);

CREATE TABLE course(
                       no INT PRIMARY KEY AUTO_INCREMENT COMMENT '강좌번호',
                       id VARCHAR(20) NOT NULL COMMENT '작성자',
                       cate VARCHAR(100) NOT NULL COMMENT '과목',
                       level int comment '학년',
                       title VARCHAR(100) COMMENT '제목',
                       content VARCHAR(2000) COMMENT '내용',
                       cnt INT DEFAULT 0 COMMENT '조회수',
                       created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '게시일',
                       ino INT NOT NULL COMMENT '강사코드 (FK)',
                       peo INT DEFAULT 0 COMMENT '현재수강인원',
                       peo_max INT COMMENT '최대 수강인원'
    -- FOREIGN KEY(id) REFERENCES user(NAME),
    -- FOREIGN KEY(ino) REFERENCES instructor(no)
);

-- 강의 테이블
-- 여러개의 영상을 하나의 강의 영상으로 묶는 테이블
CREATE TABLE lecture(
                        no INT PRIMARY KEY AUTO_INCREMENT COMMENT '강의번호',
                        cno INT NOT NULL COMMENT '강좌번호',
                        title VARCHAR(50) NOT NULL COMMENT '강의 제목',
                        explan VARCHAR(100) NOT NULL COMMENT '강의 설명',
                        keyword VARCHAR(50) COMMENT '키워드',
                        FOREIGN KEY(cno) REFERENCES course(NO)
);

-- 강사, 선생님 테이블
CREATE TABLE instructor(
                           NO INT PRIMARY KEY AUTO_INCREMENT, -- 선생님 번호
                           NAME VARCHAR(10), -- 선생님 이름
                           tel VARCHAR(20), -- 선생님 전화번호
                           email VARCHAR(100), -- 선생님 이메일
                           intro VARCHAR(1000), -- 선생님 소개글
                           cate VARCHAR(20), -- 선생님 담당 과목
                           img VARCHAR(1000), -- 선생님 프로필 이미지
                           id VARCHAR(50) -- 선생님 아이디
    -- FOREIGN KEY(id) REFERENCES user(id) ON DELETE CASCADE -- 선생님 아이디 외래키
);

-- 강의 영상 정보 db
CREATE TABLE fileobj (
                         no int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '영상번호',
                         cno INT COMMENT '강좌 번호',
                         lno INT COMMENT '강의 번호',
                         savefolder VARCHAR(400) COMMENT '저장경로',
                         originfile VARCHAR(400) COMMENT '실제 파일 이름',
                         savefile VARCHAR(800) COMMENT '난수화된 저장 파일이름',
                         filesize INT COMMENT '파일 사이즈',
                         uploaddate VARCHAR(100),
                         FOREIGN KEY(cno) REFERENCES course(no),
                         FOREIGN KEY(lno) REFERENCES lecture(no)
);

-- 강좌 썸네일 정보 db
CREATE TABLE cimgobj (
                         no int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '이미지번호',
                         cno INT COMMENT '강좌 번호',
                         savefolder VARCHAR(400) COMMENT '저장경로',
                         originfile VARCHAR(400) COMMENT '실제 파일 이름',
                         savefile VARCHAR(800) COMMENT '난수화된 저장 파일이름',
                         filesize INT COMMENT '파일 사이즈',
                         created_time VARCHAR(100),
                         FOREIGN KEY(cno) REFERENCES course(no)
);

-- 강의 썸네일 정보 db
CREATE TABLE limgobj (
                         no int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '이미지번호',
                         lno INT COMMENT '강의 번호',
                         savefolder VARCHAR(400) COMMENT '저장경로',
                         originfile VARCHAR(400) COMMENT '실제 파일 이름',
                         savefile VARCHAR(800) COMMENT '난수화된 저장 파일이름',
                         filesize INT COMMENT '파일 사이즈',
                         created_time VARCHAR(100),
                         FOREIGN KEY(lno) REFERENCES lecture(no)
);

-- 강의 문제 정보 db
-- 아직 구상중
CREATE TABLE lectest (
                         no int NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '자료번호',
                         lno INT COMMENT '강의 번호',
                         savefolder VARCHAR(400) COMMENT '저장경로',
                         originfile VARCHAR(400) COMMENT '실제 파일 이름',
                         savefile VARCHAR(800) COMMENT '난수화된 저장 파일이름',
                         filesize INT COMMENT '파일 사이즈',
                         created_time VARCHAR(100),
                         FOREIGN KEY(lno) REFERENCES lecture(no)
);

-- board
CREATE TABLE board(
                      no INT PRIMARY KEY AUTO_INCREMENT,   -- (게시글 번호) 자동 발생
                      type ENUM('notice','qna','fileboard') NOT NULL,   -- 게시글 타입: notice, faq, forum, fileboard
                      title VARCHAR(200) NOT NULL,   -- (게시글 제목)
                      content VARCHAR(1000),   -- (게시글 내용)
                      author VARCHAR(16) NOT NULL,   -- (작성자)
                      files VARCHAR(255),
                      regdate TIMESTAMP DEFAULT CURRENT_TIMESTAMP,   -- (등록일)
                      visited INT DEFAULT 0   -- (조회수)
);


-- 교재
-- 초등
CREATE TABLE elementary(
                           NO INT PRIMARY KEY AUTO_INCREMENT, -- 교재 번호
                           title VARCHAR(100) NOT NULL, -- 교재 제목
                           content VARCHAR(2000) NOT NULL, -- 교재 내용
                           price DECIMAL(10, 2), -- 가격
                           publish DATE , -- 출간일
                           resdate DATETIME DEFAULT CURRENT_TIMESTAMP -- 게시일
);

-- 중학
CREATE TABLE middle(
                       NO INT PRIMARY KEY AUTO_INCREMENT, -- 교재 번호
                       title VARCHAR(100) NOT NULL, -- 교재 제목
                       content VARCHAR(2000) NOT NULL, -- 교재 내용
                       price DECIMAL(10, 2), -- 가격
                       publish DATE , -- 출간일
                       resdate DATETIME DEFAULT CURRENT_TIMESTAMP -- 게시일
);

-- 수능
CREATE TABLE high(
                     NO INT PRIMARY KEY AUTO_INCREMENT, -- 교재 번호
                     title VARCHAR(100) NOT NULL, -- 교재 제목
                     content VARCHAR(2000) NOT NULL, -- 교재 내용
                     price DECIMAL(10, 2), -- 가격
                     publish DATE , -- 출간일
                     resdate DATETIME DEFAULT CURRENT_TIMESTAMP -- 게시일
);

-- 토익
CREATE TABLE toeic(
                      NO INT PRIMARY KEY AUTO_INCREMENT, -- 교재 번호
                      title VARCHAR(100) NOT NULL, -- 교재 제목
                      content VARCHAR(2000) NOT NULL, -- 교재 내용
                      price DECIMAL(10, 2), -- 가격
                      publish DATE , -- 출간일
                      resdate DATETIME DEFAULT CURRENT_TIMESTAMP -- 게시일
);

-- 교재 업로드
CREATE TABLE bookobj(
                        NO INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
                        bno INT NOT NULL, -- 교재 번호
                        savefolder VARCHAR(400),
                        originfile VARCHAR(400),
                        filesize LONG,
                        uploaddate VARCHAR(100)
);
