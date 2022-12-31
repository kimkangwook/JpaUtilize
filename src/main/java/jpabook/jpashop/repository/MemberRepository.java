package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class MemberRepository {

    // 스프링이 생성한 EntityManager을 주입받음
    @PersistenceContext
    private EntityManager em;


    // EntityMangerFactory 주입받기
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    public void save(Member member) {
        em.persist(member);
        //영속성 컨텍스트에 member를 넣음, 나중에 트랜잭션이 커밋되는 시점에 DB에 INSERT 쿼리가 날라가서 반영됨
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
        // 단건조회
        // find(타입, PK)
    }

    //JPQL 사용
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
        // SQL과의 차이 : SQL은 테이블을 대상으로 조회함, JPQL은 Entity 객체를 대상으로 조회함
        //               alias = m;
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name= :name", Member.class)
                .setParameter("name", name).getResultList();
        // 파라미터 바인딩
    }


}
