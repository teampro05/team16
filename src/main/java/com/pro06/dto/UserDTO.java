package com.pro06.dto;

import com.pro06.entity.BaseEntity;
import com.pro06.entity.Role;
import com.pro06.entity.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class UserDTO {

    private Integer no;

    @Column(length = 100)
    private String id;          //회원 아이디

    @Column(length = 1000)
    private String pw;          //비밀번호

    @Column(length = 100)
    private String name;        //회원 이름

    @Column(length = 100)
    private String tel;         //전화번호

    @Column(length = 100)
    private String email;       //이메일

    @Column(length = 100)
    private String addr1;       //주소1

    @Column(length = 100)
    private String addr2;       //주소2

    @Column(length = 100)
    private String postcode;    //주소번호

    @Column(nullable = true)
    private String img;         //회원 이미지

    @Column(nullable = true)
    private LocalDateTime loginAt;  //최종 로그인시간

    @Enumerated(EnumType.STRING)
    private Status status;      //회원 활동상태

    @Enumerated(EnumType.STRING)
    private Role role;          //회원 권한

    private LocalDateTime createdTime;

    private LocalDateTime modifiedTime;

    }
