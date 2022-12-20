package com.java.securecoding.controller;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.domain.form.BoardForm;
import com.java.securecoding.domain.member.Member;
import com.java.securecoding.domain.session.MemberInfo;
import com.java.securecoding.service.BoardService;
import com.java.securecoding.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BoardController {

    private final MemberService memberService;
    private final BoardService boardService;

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

        System.out.println("board = " + board);
        System.out.println("board.getContent() = " + board.getContent());
        System.out.println("board.getSubject() = " + board.getSubject());
        System.out.println("board.getFileName() = " + board.getFileName());

        System.out.println("board.getFilePath() = " + board.getFilePath());

        boardService.saveBoard(board, file);

        model.addAttribute("message", "글 등록이 완료되었습니다.");
        model.addAttribute("searchUrl", "/");

        return "message";
    }

    @PostMapping("/1/6/secure")
    public String CreateBoardForm_secure(BoardForm form, Model model,
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

        System.out.println("board.getContent() = " + board.getContent());
        System.out.println("board.getSubject() = " + board.getSubject());
        System.out.println("board.getFileName() = " + board.getFileName());

        System.out.println("board.getFilePath() = " + board.getFilePath());


        boardService.saveBoard_secure(board, file);

        model.addAttribute("message", "글 등록이 완료되었습니다.");
        model.addAttribute("searchUrl", "/");

        return "message";
    }
}
