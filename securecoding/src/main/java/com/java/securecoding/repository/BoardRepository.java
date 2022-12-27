package com.java.securecoding.repository;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findAllById(Long id);
}
