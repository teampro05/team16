package com.pro06.entity;

import com.pro06.dto.BoardDTO;
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

    @Column(nullable = false)
    private String title;   //제목

    @Column(nullable = false)
    private String content; //내용

    @Column(nullable = false)
    private String author;  //저자



    public static Notice create(BoardDTO boardDTO) {
        Notice notice = new Notice();
        notice.setTitle(boardDTO.getTitle());
        notice.setContent(boardDTO.getContent());
        notice.setAuthor(boardDTO.getAuthor());
        return notice;
    }
}
