package jpabook.jpashop.repository;


import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAll(OrderSearch orderSearch) {
        // 동적 쿼리(status나 name이 null일 경우 다가져와)
        // 1. 동적 쿼리를 위해 JPQL을 수동으로 문자열로 만들기는 불가능함
        // 2. JPA Criteria를 사용하지않음(유지보수에 안좋음)
        // 3. Querydsl 사용함
        return em.createQuery("select o from Order o join o.member m where o.status= :status and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
//                .setFirstResult(100)  //페이징 관련
                .setMaxResults(1000) // 가져오는 최대 갯수 1000건
                .getResultList();
    }


    /**
     * JPA Criteria
     */
    //JPQL을 자바코드로 작성하게 도와주는 것을 JPA에서 표준으로 제공함
    //
    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Object, Object> m = o.join("member", JoinType.INNER);

        List<Predicate> criteria = new ArrayList<>();

        // 주문 상태 검색
        if(orderSearch.getOrderStatus()!=null) {
            Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
            criteria.add(status);
        }

        // 회원 이름 검색
        if(StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name = cb.like(m.<String>get("name"), "%"+orderSearch.getMemberName()+"%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
        return query.getResultList();
    }

}
