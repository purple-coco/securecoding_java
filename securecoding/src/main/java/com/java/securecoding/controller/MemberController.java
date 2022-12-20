package com.java.securecoding.controller;


import com.java.securecoding.domain.form.MemberForm;
import com.java.securecoding.domain.form.MemberForm2;
import com.java.securecoding.domain.member.Member;
import com.java.securecoding.domain.session.MemberInfo;
import com.java.securecoding.service.CommandService;
import com.java.securecoding.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController{

    private final MemberService memberService;

    @GetMapping(value = {"/member/signup", "/2/5", "/2/5/vuln", "/2/5/secure"})
    public String CreateMemberForm(Model model) {

        model.addAttribute("memberForm", new MemberForm());

        return "/2/2.5";
    }

    @PostMapping(value = {"/2/5/vuln"})
    public String CreateMemberForm_vuln(@Valid MemberForm form, BindingResult result, Model model) {

        log.info("bindingresult = {}", result);

        if (result.hasErrors()) {
            model.addAttribute("message", "유효하지 않은 정보입니다.");
            model.addAttribute("searchUrl", "/2/5");

            return "message";
        }

        Member member = new Member();

        member.setName(form.getName());
        member.setUsername(form.getUsername());


        if(!memberService.passwordValidate(form.getPassword())) {

            model.addAttribute("message", "최소 8자 이상, 영어 대·소문자, 숫자, 특수문자가 혼용되어야 합니다.");
            model.addAttribute("searchUrl", "/member/signup");

            return "message";
        } else {
            member.setPassword(form.getPassword());

            memberService.join(member);

            model.addAttribute("message", "회원가입이 완료되었습니다.");
            model.addAttribute("searchUrl", "/member/login");

            return "message";
        }
    }

    @PostMapping(value = {"/2/5/secure"})
    public String CreateMemberForm_secure(@Valid MemberForm2 form, BindingResult result, Model model) {

        if (result.hasErrors()) {
            log.info("bindingresult = {}", result);
            model.addAttribute("message", "유효하지 않은 정보입니다.");
            model.addAttribute("searchUrl", "/2/5");

            return "message";
        }

        Member member = new Member();

        member.setName(form.getName());
        member.setUsername(form.getUsername());

        System.out.println("member = " + member);

        if(memberService.passwordValidate(form.getPassword())) {

            member.setPassword(memberService.HashPassword(form.getPassword()));
            memberService.join(member);

            model.addAttribute("message", "회원가입이 완료되었습니다.");
            model.addAttribute("searchUrl", "/member/login");

            return "message";
        } else {
            model.addAttribute("message", "최소 8자 이상, 영어 대·소문자, 숫자, 특수문자가 혼용되어야 합니다.");
            model.addAttribute("searchUrl", "/member/signup");

            return "message";
        }
    }

    /* 회원 정보 수정 */
    @GetMapping(value={"/mypage/info/{memberId}", "/mypage/info/{memberId}/vuln", "/mypage/info/{memberId}/secure"})
    public String updateMemberForm_id(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId);

        MemberForm form = new MemberForm();

        form.setId(member.getId());
        form.setName(member.getName());
        form.setUsername(member.getUsername());

        model.addAttribute("form", form);

        return "/2/2.1";
    }

    @PostMapping("/mypage/info/{memberId}/vuln")
    public String updateMemberForm_id_vuln(@PathVariable("memberId") Long memberId,
                                    HttpServletRequest request, @ModelAttribute("form") MemberForm form) {

        memberService.updateMemberInfo_vuln(memberId, form.getName(), form.getPassword());

        return "/2/2.1";
    }

    @PostMapping("/mypage/info/{memberId}/secure")
    public String updateMemberForm_id_secure(@PathVariable("memberId") Long memberId,
                                      HttpServletRequest request, @ModelAttribute("form") MemberForm form, Model model) {

        MemberInfo member = (MemberInfo) request.getSession().getAttribute("memberInfo");
        Member findMember = memberService.findByUsername(form.getUsername());

        memberService.validateUpdate(member.getId(), memberId);

        memberService.updateMemberInfo_secure(memberId, form.getName(), form.getPassword());

        model.addAttribute("message", "회원 정보가 수정되었습니다.");
        model.addAttribute("searchUrl", "/");

        return "message";

    }

    /* 회원 탈퇴 */
    @GetMapping(value = {"/mypage/delete/{memberId}", "/mypage/delete/{memberId}/vuln", "/mypage/delete/{memberId}/secure"})
    public String deleteMemberForm_id(@PathVariable("memberId") Long memberId, Model model) {
        Member member = memberService.findOne(memberId);

        MemberForm form = new MemberForm();

        form.setName(member.getName());
        form.setUsername(member.getUsername());

        model.addAttribute("form", form);

        return "/1/1.15";
    }

    @PostMapping("/mypage/delete/{memberId}/vuln")
    public String deleteMemberForm_vuln(@PathVariable("memberId") Long memberId, HttpServletRequest request, Model model, HttpSession session) {

        String id = request.getParameter("id");

        if (id == null) {
            return "/1/1.15";
        }

        Long memberid = Long.parseLong(id);

        memberService.deleteMember(memberid);

        request.getSession().removeAttribute("memberInfo");
        session.invalidate();

        model.addAttribute("message", "정말 탈퇴하시겠습니까?");
        model.addAttribute("searchUrl", "/");

        return "message";
    }

    @PostMapping("/mypage/delete/{memberId}/secure")
    public String deleteMemberForm_secure(@PathVariable("memberId") Long memberId, HttpServletRequest request, HttpSession session, Model model) {

        MemberInfo member = (MemberInfo) request.getSession().getAttribute("memberInfo");

        memberService.validateUpdate(member.getId(), memberId);

        memberService.deleteMember(memberId);

        request.getSession().removeAttribute("memberInfo");
        session.invalidate();

        model.addAttribute("message", "정말 탈퇴하시겠습니까?");
        model.addAttribute("searchUrl", "/");

        return "message";

    }


    /* 로그아웃 */
    @GetMapping("member/logout")
    public String logoutMember(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
        request.getSession().removeAttribute("memberInfo");
        session.invalidate();

        return "redirect:/";
    }

}
