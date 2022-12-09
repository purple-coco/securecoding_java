package com.java.securecoding.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberLoginForm2 {
    private String name;
    private String password;
    private int count;
    private boolean islocked;
}
