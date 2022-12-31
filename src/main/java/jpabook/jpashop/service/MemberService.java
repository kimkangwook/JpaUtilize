package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//@AllArgsConstructor
@RequiredArgsConstructor
//기본적으로 비즈니스로직은 트랜잭션을 사용해야함
//클래스 레벨에서 Transactional(readOnly=true)하고 데이터 변경 메서드에서 따로 Transactional 선언해서 사용함
public class MemberService {

    private final MemberRepository memberRepository;



    /**
     * 회원 가입
     */
    @Transactional // 기본값은 readOnly=false임
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
        // 영속성 컨텍스트에 등록할 때 (key, value) 형식으로 저장하는데 key값에 해당 객체의 pk값을 저장함
        // 고로 DB에 저장하기 전에 member의 id 값이 정해져서 들어감
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        // 이 로직도 위험함. WAS가 멀티쓰레드 환경에서 동시에 이 로직을 수행했을 때 같은 ID로 가입할 수 있음
        // 그래서 최후의 방어선으로 DB에서 member.name이 Unique하다는 제약조건을 줘야함
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    //회원 전체 조회
    //트랜잭션을 조회하는 메서드에서 readOnly=true를 선언하면 성능을 최적화함, 데이터 변경 메서드에서 사용하면 안됨(변경이 안됨)
//    @Transactional(readOnly=true)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
//    @Transactional(readOnly=true)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
