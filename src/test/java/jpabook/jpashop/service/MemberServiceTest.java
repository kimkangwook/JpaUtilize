package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class) // junit 실행할 때 스프링이랑 같이 실행해줌
@SpringBootTest // 스프링부트 띄우는 상태에서 돌리기
@Transactional //테스트case에서 스프링은 아예 커밋하지않고 롤백함, jpa에서는 트랜잭션 중 커밋되면 DB에 영속성 컨텍스트들을 INSERT 쿼리로 보냄
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

//    @Autowired
//    EntityManager em;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
//        em.flush(); // flush() : 영속성 컨텍스트의 변경이나 등록내용을 DB에 반영하는 메소드
        assertEquals(member, memberRepository.findOne(savedId));
     }

     @Test
     public void 중복_회원_예외() throws Exception {
         //given
         Member member1 = new Member();
         member1.setName("kim");

         Member member2 = new Member();
         member2.setName("kim");
         //when
         memberService.join(member1);
         assertThrows(IllegalStateException.class,()->memberService.join(member2)); //예외가 발생해야 한다!!

//
         //then
//         fail("예외가 발생해야 한다."); // 여기 문장이 실행되면 테스트가 실패하는 것
      }

}