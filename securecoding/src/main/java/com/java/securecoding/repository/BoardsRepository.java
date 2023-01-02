package com.java.securecoding.repository;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.domain.board.BoardSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardsRepository {

    private final EntityManager entityManager;


    public List<Board> findAll_vuln(BoardSearch boardSearch) {

        String query;
        if (boardSearch.isSubjectNull()) {
            query = "SELECT * FROM BOARD ORDER BY BOARD_ID DESC";
        } else {
            query = "SELECT * FROM BOARD WHERE SUBJECT LIKE '%" + boardSearch.getSubject() + "%'" + "ORDER BY BOARD_ID DESC";
        }

        List<Board> boards = entityManager.createNativeQuery(query, Board.class).setFirstResult((boardSearch.getPage()-1) * 10).setMaxResults(10).getResultList();

        return boards;
    }
}
