package com.pro06.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "oto")
@ToString
public class Oto extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    @Column(length = 100)
    private String title;   //제목

    @Column(length = 1000)
    private String content; //내용

    @Column(length = 30)
    @NotNull
    private String email;
    }
