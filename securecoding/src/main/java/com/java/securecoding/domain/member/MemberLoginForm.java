package com.java.securecoding.domain.member;

import lombok.Getter;
import lombok.Setter;

/*제2절 보안기능 16.반복된 인증시도 제한 기능 부재 Vulnerable 로그인 폼*/
@Getter
@Setter
public class MemberLoginForm {
    private String username;
    private String password;
}
