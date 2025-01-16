package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="space_id",referencedColumnName = "space_id",nullable = false)
    private Space space;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "user_id",nullable = false)
    private User user;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items;






}
