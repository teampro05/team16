package com.pro06.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@Table(name = "bookImg")
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class BookImg extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;

    private String savefolder;
    private String originfile;
    private String savefile;
    private Long filesize;

    // 여러 개의 다른 Entity 인스턴스가 이 Entity의 하나의 Book 필드에 매핑
    @ManyToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    // 부모 Entity가 자식 Entity에 어떤 영향을 미치는지
    // Remove: 부모 Entity가 삭제되면 자식 Entity도 함께 삭제
    @JoinColumn(name = "bno", referencedColumnName = "no")
    // bookNumber -> bno
    // bno 컬럼은 현재 Entity에서 외래 키로 사용, 이를 통해 Book Entity의 no 컬럼과 매핑
    private Book book;
}
