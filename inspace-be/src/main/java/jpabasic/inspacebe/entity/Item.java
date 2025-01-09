package jpabasic.inspacebe.entity;

import jakarta.persistence.*;
import jpabasic.inspacebe.converter.CtypeAttributeConverter;

@Entity
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int item_id;
    private String title;
    @Convert(converter= CtypeAttributeConverter.class)
    private String ctype;
    private String image_url;
    private String contents_url;
    private float position_x;
    private float position_y;
    private float height;
    private float width;

}
