package com.java.securecoding.domain.form;

import com.java.securecoding.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@ToString
public class BoardForm {

    private Long id;

    @NotEmpty(message = "제목을 입력해주세요")
    private String subject;

    @NotEmpty(message = "내용을 입력해주세요")
    private String content;

    private Member member;

    private String fileName;

    private String filePath;

}
