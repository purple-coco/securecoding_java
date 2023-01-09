package com.java.securecoding.repository;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.domain.board.BoardSearch;
import com.java.securecoding.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findAllById(Long id);

    /* 게시판 제목 검색 기능 */
    Page<Board> findBySubjectContaining(@RequestParam("keyword") String keyword, Pageable pageable);

}
