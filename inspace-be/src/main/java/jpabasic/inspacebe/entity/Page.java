package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="page_id")
    private Integer pageId;

    @Column(name="page_number")
    private int pageNumber;

//    @ManyToOne(fetchType=FetchType.LAZY)
//    @JoinColumn(name="space_id",referencedColumnName = "space_id",nullable = false)
//    private
}
