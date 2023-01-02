package com.java.securecoding.domain.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardSearch {

    private String memberName;
    private String subject;
    private String content;
    private String keyword;
    private Integer page;

    public BoardSearch() {}

    public boolean isSubjectNull() { return this.subject == null; }
}
