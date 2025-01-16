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
    @JoinColumn(name="space_id",referencedColumnName = "space_id")
    private Space space;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id",referencedColumnName = "user_id")
    private User user;

    @OneToMany(mappedBy = "page",cascade = CascadeType.ALL) //cascade 값 변경 - 민서
    private List<Item> items;


    //아카이브에서 아이템 삭제하는 경우
    //자식 객체는 그대로 두고, page와의 매핑관계만 끊기
    public void removeItem(Item item) {
        this.items.remove(item);
        item.setPage(null);
    }






}
