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
    private Long no;

    @Column(length = 100)
    private String title;

    @Column(length = 1000)
    private String content;

    @Column(nullable = false)
    private String author;



    public static Notice create(BoardDTO boardDTO) {
        Notice notice = new Notice();
        notice.setTitle(boardDTO.getTitle());
        notice.setContent(boardDTO.getContent());
        notice.setAuthor(boardDTO.getAuthor());
        return notice;
    }
}
