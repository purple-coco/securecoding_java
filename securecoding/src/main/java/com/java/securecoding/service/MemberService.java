package com.java.securecoding.service;


import com.java.securecoding.domain.member.Member;
import com.java.securecoding.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mindrot.jbcrypt.BCrypt;
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
        validateDuplicateMember(member.getUsername());
        memberRepository.save(member);

        return member.getId();
    }

    private boolean validateDuplicateMember(String username) {
        return memberRepository.existsByUsername(username);
    }

//    안전한 비밀번호 생성 규칙 적용
    public boolean passwordValidate(String inputPassword) {
        String passwordPolicy = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

        Pattern pattern = Pattern.compile(passwordPolicy);
        Matcher matcher = pattern.matcher(inputPassword);

        return matcher.matches();
    }

    public String HashPassword(String inputPassword) {
        String encryptedPassword = BCrypt.hashpw(inputPassword, BCrypt.gensalt(10));

        return encryptedPassword;
    }

    public boolean checkPassword(String inputPassword, String encryptedPassword) {
        if (BCrypt.checkpw(inputPassword, encryptedPassword)) {
            return true;
        } else {
            return false;
        }
    }



}
