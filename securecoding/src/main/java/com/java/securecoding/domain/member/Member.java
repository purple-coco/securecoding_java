package com.java.securecoding.domain.member;

import com.java.securecoding.domain.board.Board;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String username;

    private String password;

    private int count;

    private boolean islocked = false;

    @OneToMany(mappedBy = "member")
    private List<Board> boards = new ArrayList<>();

    public void updateMemberInfo(String name, String password) {
        setName(name);
        setPassword(password);
    }

}
