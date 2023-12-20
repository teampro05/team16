package com.pro06.entity;

import com.pro06.dto.BoardDTO;
import com.pro06.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CurrentTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
@ToString
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @Column(unique = true)
    private String id;          //회원 아이디

    @Column(nullable = false)
    private String pw;          //비밀번호

    @Column(nullable = false)
    private String name;        //회원 이름

    @Column(nullable = false)
    private String tel;         //전화번호

    @Column(nullable = false)
    private String email;       //이메일

    @Column(nullable = false)
    private String addr1;       //주소1

    @Column(nullable = true)
    private String addr2;       //주소2

    @Column(nullable = true)
    private String postcode;    //주소번호

    @Column(nullable = true)
    private String img;         //회원 이미지

    @CreatedDate
    private LocalDateTime loginAt;  //최종 로그인시간

    @Enumerated(EnumType.STRING)
    private Status status;      //회원 활동상태

    @Enumerated(EnumType.STRING)
    private Role role;          //회원 권한

}
