package com.pro06.entity;

import com.pro06.dto.BoardDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("'n'")
    private String deleteyn; // 삭제 여부


}
