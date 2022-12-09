package com.java.securecoding.domain.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

/*제2절 보안기능 5.암호화되지 않은 중요정보 Secure 회원가입 폼*/
@Getter
@Setter
public class MemberForm2 {
    private Long id;

    @NotEmpty(message = "이름을 입력해주세요")
    private String name;

    @NotEmpty(message = "아이디를 입력해주세요")
    private String username;

    @NotEmpty(message = "비밀번호를 입력해주세요")
    private String password;

    private Integer login_count;

    private boolean islocked = false;

}
