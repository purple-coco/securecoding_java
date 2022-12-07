package com.java.securecoding.service;


import com.java.securecoding.domain.member.Member;
import com.java.securecoding.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    public Long join(Member member) {
        memberRepository.save(member);

        return member.getId();
    }

    private boolean validateDuplicateMember(String username) {
        return memberRepository.existsByUsername(username);
    }

//    안전한 비밀번호 생성 규칙 적용
    public boolean passwordValidate(String password) {
        String passwordPolicy = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

        Pattern pattern = Pattern.compile(passwordPolicy);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public String encryptedPassword(String password) {
        return "";
    }



}
