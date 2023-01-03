package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    // @Valid로 memberForm에 들어있는 NotEmpty와 같은 어노테이션이 인식될 수 있게 해줌
    // 원래 name이 없이 이 컨트롤러 메소드로 들어오면 에러가 나서 팅기는데 뒤 파라미터로 BindingResult가 오면 에러가 binding된채로 코드가 돌아감
    public String create(@Valid MemberForm form, BindingResult result) {

        if (result.hasErrors()) {
            // 바인딩 에러를 return 화면까지 끌고가줌
            // thymeleaf - spring 결합
            return "members/createMemberForm";
        }



        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }
}
