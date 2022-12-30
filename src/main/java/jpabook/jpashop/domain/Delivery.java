package jpabook.jpashop.domain;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id @GeneratedValue
    @Column(name="delivery_id")
    private Long id;

    @OneToOne(fetch = LAZY, mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // Enum쓸때 어노테이션 붙이기 - EnumType에는 String 과 ordinal이 있는데 절대 ordinal쓰면안됨(순서에 밀려날 일 없음)
    private DeliveryStatus status; //READY, COMP
}
