package com.pro06.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notice")
@ToString
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    @Column(length = 20)
    private String title;
    @Column(length = 1000)
    private String content;


    public static Notice create(BoardDTO boardDTO) {
        Notice notice = new Notice();
        notice.setTitle(boardDTO.getTitle());
        notice.setContent(boardDTO.getContent());
        return notice;
    }
}
