package com.java.securecoding.controller;

import com.java.securecoding.domain.form.MemberForm;
import com.java.securecoding.domain.member.Member;
import com.java.securecoding.domain.member.MemberLoginForm;
import com.java.securecoding.domain.member.MemberLoginForm2;
import com.java.securecoding.domain.session.MemberInfo;
import com.java.securecoding.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;

    @RequestMapping("/")
    public String main() {
        return "index";
    }

    @GetMapping(value = {"/member/login", "/2/16"})
    public String LoginForm(Model model) {

        model.addAttribute("memberForm", new MemberForm());

        return "/2/2.16";
    }

    @PostMapping(value = {"/2/16/vuln"})
    public String LoginForm_vuln(@Valid @ModelAttribute("form")MemberLoginForm form,
                                 HttpServletRequest request, HttpServletResponse response, Model model) {

        Member findMember = memberService.findByUsername(form.getUsername());

        log.info("findMember {}", findMember);


        if(findMember == null) {
            model.addAttribute("message", "아이디, 비밀번호를 확인해주세요");
            model.addAttribute("searchUrl", "/member/login");

            return "message";
        } else {
            if (form.getPassword().equals(findMember.getPassword()) ||
                    memberService.checkPassword(form.getPassword(), findMember.getPassword())) { /* 비밀번호 DB 평문 저장 */
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setId(findMember.getId());
                memberInfo.setUsername(findMember.getUsername());

                HttpSession session = request.getSession(true);
                session.setAttribute("memberInfo", memberInfo);

                return "index";
            } else if (!form.getPassword().equals(findMember.getPassword())) {
                model.addAttribute("message", "아이디, 비밀번호를 확인해주세요");
                model.addAttribute("searchUrl", "/member/login");

                return "message";
            }
        }
        return "/2/2.16";
    }

    @PostMapping(value = {"/member/login", "/2/16/secure"})
    public String LoginForm_secure(@Valid @ModelAttribute("form")MemberLoginForm2 form,
                                   HttpServletRequest request, HttpServletResponse response, Model model) {

        Member findMember = memberService.findByUsername(form.getUsername());

        if (findMember == null) {
            model.addAttribute("message", "아이디, 비밀번호를 확인해주세요1");
            model.addAttribute("searchUrl", "/member/login");

            return "message";
        }

        else {
            if (form.getPassword().equals(findMember.getPassword())
                    || (memberService.checkPassword(form.getPassword(), findMember.getPassword())
                    && !findMember.isIslocked())) {
                MemberInfo memberInfo = new MemberInfo();
                memberInfo.setId(findMember.getId());
                memberInfo.setUsername(findMember.getUsername());

                HttpSession session = request.getSession(true);
                session.setAttribute("memberInfo", memberInfo);

                if (findMember.getCount() != 0) {
                    memberService.clearLoginCount(findMember.getUsername());
                }

                return "redirect:/";

            } else {
                if (findMember.isIslocked()) {
                    model.addAttribute("message", "잠긴 계정입니다.");

                } else {
                    memberService.updateFailure(form.getUsername());
                    model.addAttribute("message", "아이디, 비밀번호를 확인해주세요3");

                }
                model.addAttribute("searchUrl", "/member/login");
                return "message";
            }
        }
    }
}
