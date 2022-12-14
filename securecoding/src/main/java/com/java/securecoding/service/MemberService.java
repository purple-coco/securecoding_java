package com.java.securecoding.service;


import com.java.securecoding.domain.member.Member;
import com.java.securecoding.repository.MemberRepository;
import exception.NotJoinException;
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

    /* 회원가입 */
    public Long join(Member member) {

        if(validateDuplicateMember(member.getUsername())) {
            throw new NotJoinException("중복된 아이디입니다.");
        } else {
            memberRepository.save(member);

            return member.getId();
        }
    }

    /* 회원 정보 조회 */
    @Transactional(readOnly = true)
    public Member findOne(Long memberId) { return memberRepository.findAllById(memberId);}

    /* 회원 아이디 조회 */
    @Transactional(readOnly = true)
    public Member findByUsername(String username) { return memberRepository.findByUsername(username);}

    /* 아이디 중복 회원 검증 */
    private boolean validateDuplicateMember(String username) {
        return memberRepository.existsByUsername(username);
    }

    /* 안전한 비밀번호 규칙 생성 적용 */
    public boolean passwordValidate(String inputPassword) {
        String passwordPolicy = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

        Pattern pattern = Pattern.compile(passwordPolicy);
        Matcher matcher = pattern.matcher(inputPassword);

        return matcher.matches();
    }

    /* 비밀번호 DB 암호화 저장 */
    public String HashPassword(String inputPassword) {
        return BCrypt.hashpw(inputPassword, BCrypt.gensalt(10));
    }

    public boolean checkPassword(String inputPassword, String encryptedPassword) {
        boolean result = false;
        try{
            result = BCrypt.checkpw(inputPassword, encryptedPassword);
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    /* 인증 시도 제한 */
    @Transactional
    public void clearLoginCount(String username) {
        Member member = findByUsername(username);
        member.setCount(0);
    }

    @Transactional
    public void updateFailure(String username) {
        Member member = findByUsername(username);
        if (member.getCount() == 4) {
            member.setIslocked(true);
        }

        memberRepository.updateCountFailure(username);
    }

    /* 회원 정보 수정 */
    @Transactional
    public void updateMemberInfo(Long memberId, String name, String password) {
        Member findMemberInfo = memberRepository.findAllById(memberId);

        findMemberInfo.setName(name);

        memberRepository.save(findMemberInfo);
    }


}
