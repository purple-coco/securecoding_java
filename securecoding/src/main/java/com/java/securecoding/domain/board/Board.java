package com.java.securecoding.domain.board;

import com.java.securecoding.domain.member.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Board {

    @Id
    @GeneratedValue
    @Column(name = "board_id")
    private Long id;

    private String subject;

    private String content;

    private LocalDate createDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean isDelete = false;

    private String fileName;

    private String filePath;

    public static Board createBoard(Member member, String subject, String content, String fileName, String filePath) {

        Board board = new Board();

        board.setMember(member);
        board.setSubject(subject);
        board.setContent(content);
        board.setCreateDate(LocalDate.now());
        board.setFileName(fileName);
        board.setFilePath(filePath);

        return board;
    }

}
