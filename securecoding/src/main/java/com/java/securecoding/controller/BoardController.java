package com.java.securecoding.controller;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.domain.form.BoardForm;
import com.java.securecoding.domain.form.BoardForm2;
import com.java.securecoding.domain.member.Member;
import com.java.securecoding.domain.session.MemberInfo;
import com.java.securecoding.service.BoardService;
import com.java.securecoding.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

    @GetMapping(value = {"/board"})
    public String BoardListForm(Model model) {

        List<Board> board = boardService.findAll();

        model.addAttribute("boards", board);

        return "/1/1.1";
    }

    @GetMapping(value = {"/board/new", "/1/6/vuln", "/1/6/secure"})
    public String CreateBoardForm(Model model) {

        BoardForm form = new BoardForm();

        model.addAttribute("form", form);

        return "/1/1.6";

    }

    @PostMapping("/1/6/vuln")
    public String CreateBoardForm_vuln(BoardForm form, Model model,
                                       MultipartHttpServletRequest request, MultipartFile file) throws IOException  {

        HttpSession session = request.getSession();

        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        Member member = memberService.findOne(memberInfo.getId());

        Board board = Board.createBoard(
                member,
                form.getSubject(),
                form.getContent(),
                form.getFileName(),
                form.getFilePath());

        boardService.saveBoard(board, file);

        model.addAttribute("message", "글 등록이 완료되었습니다.");
        model.addAttribute("searchUrl", "/");

        return "message";
    }

    @PostMapping("/1/6/secure")
    public String CreateBoardForm_secure(BoardForm2 form, Model model,
                                         MultipartHttpServletRequest request, MultipartFile file) throws IOException {
        HttpSession session = request.getSession();

        MemberInfo memberInfo = (MemberInfo) session.getAttribute("memberInfo");
        Member member = memberService.findOne(memberInfo.getId());

        Board board = Board.createBoard(
                member,
                form.getSubject(),
                form.getContent(),
                form.getFileName(),
                form.getFilePath());

        boardService.saveBoard_secure(board, file);

        model.addAttribute("message", "글 등록이 완료되었습니다.");
        model.addAttribute("searchUrl", "/");

        return "message";
    }

    @GetMapping("/board/{boardId}/vuln")
    public String getBoardForm_vuln(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);

        model.addAttribute("form", board);

        return "/board/getBoardForm_vuln";
    }

    @GetMapping("/board/delete/{boardId}/vuln")
    public String deleteBoard_vuln(@PathVariable("boardId") Long boardId, BoardForm form, HttpServletRequest request, Model model) {

        //게시물 삭제 시 사용자 검증 없이 삭제 가능
        //2절 보안기능 2.부적절한 인가 --> 세션정보 통해 사용자 검증
        MemberInfo member = (MemberInfo) request.getSession().getAttribute("memberInfo");
        boardService.validateUpdate(member.getId(), boardId);

        model.addAttribute("message", "삭제하시겠습니까?");
        model.addAttribute("searchUrl", "/boards");

        boardService.deleteBoard(boardId);

        return "message";

    }

    @GetMapping("/board/delete/{boardId}/secure")
    public String deleteBoard_secure(@PathVariable("boardId") Long boardId, BoardForm form, HttpServletRequest request, Model model) {

        model.addAttribute("message", "삭제하시겠습니까?");
        boardService.deleteBoard(boardId);

        model.addAttribute("searchUrl", "/boards");


        return "message";

    }


}
