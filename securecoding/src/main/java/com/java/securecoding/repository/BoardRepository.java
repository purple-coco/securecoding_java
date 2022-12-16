package com.java.securecoding.repository;

import com.java.securecoding.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
