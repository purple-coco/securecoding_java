package com.java.securecoding.controller;

import com.java.securecoding.domain.board.Board;
import com.java.securecoding.domain.board.BoardSearch;
import com.java.securecoding.domain.form.BoardForm;
import com.java.securecoding.domain.form.BoardForm2;
import com.java.securecoding.domain.member.Member;
import com.java.securecoding.domain.session.MemberInfo;
import com.java.securecoding.exception.PermissionException;
import com.java.securecoding.service.BoardService;
import com.java.securecoding.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    /* 게시글 목록 */
    @GetMapping(value = {"/board/vuln"})
    public String BoardListForm_vuln(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                                @RequestParam(value = "page", required = false) Integer page) {

        BoardSearch boardSearch = new BoardSearch();

        if (page == null) {
            page = 1;
        }

        if (keyword != null) {
            page = 1;
            boardSearch.setSubject(keyword);
        }

        boardSearch.setPage(page);

        List<Board> boards = boardService.findAll_vuln(boardSearch);
        int size = boardService.findAll().size();

        model.addAttribute("boards", boards);
        model.addAttribute("page", boardSearch.getPage());
        model.addAttribute("size", size);

        return "/1/1.1";
    }


    /* 게시글 목록 */
    @GetMapping(value = {"/board/secure"})
    public String BoardListForm_secure(Model model, @RequestParam(value = "keyword", required = false) String keyword,
                                @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Board> boardlist = boardService.pageList(pageable);

        int nowPage = boardlist.getPageable().getPageNumber() + 1;
        int startPage = Math.max(nowPage - 4, 1);
        int endPage = Math.min(nowPage + 5, boardlist.getTotalPages());


        if (keyword == null) {
            model.addAttribute("boards", boardService.pageList(pageable));
            model.addAttribute("nowPage", nowPage);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);
        } else {
            model.addAttribute("boards", boardService.searchList(keyword, pageable));
        }

        return "/1/1.1";
    }

    @GetMapping("/board")
    public String BoardListForm(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        model.addAttribute("boards", boardService.pageList(pageable));

        return "/1/1.1";
    }

    @GetMapping("/2/2")
    public String BoardListForm_(Model model, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        model.addAttribute("boards", boardService.pageList(pageable));

        return "/2/2.2";
    }

    /* 게시글 생성 */
    @GetMapping(value = {"/board/new", "/1/6/vuln", "/1/6/secure"})
    public String CreateBoardForm(Model model) {

        BoardForm form = new BoardForm();

        model.addAttribute("form", form);

        return "/1/1.6";

    }

    @GetMapping(value = {"/1/11", "/1/11/vuln", "/1/11/secure"})
    public String CreateBoardForm_CSRF(Model model) {

        BoardForm form = new BoardForm();

        model.addAttribute("form", form);

        return "/1/1.11";

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

        String param = request.getParameter("_csrf");

        if (request.getSession().getAttribute("CSRF_TOKEN").equals(param)) {

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
            model.addAttribute("searchUrl", "/board");

            return "message";
        } else {
            throw new PermissionException("권한이 없습니다.");
        }
    }

    @PostMapping("/1/11/vuln")
    public String CSRFForm_vuln(BoardForm form, Model model,
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
        model.addAttribute("searchUrl", "/board");

        return "message";
    }

    @PostMapping("/1/11/secure")
    public String CSRFForm_secure(BoardForm2 form, Model model,
                                MultipartHttpServletRequest request, MultipartFile file) throws IOException  {

        HttpSession session = request.getSession();

        String param = request.getParameter("_csrf");

        if (request.getSession().getAttribute("CSRF_TOKEN").equals(param)) {

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
            model.addAttribute("searchUrl", "/board");

            return "message";
        } else {
            throw new PermissionException("권한이 없습니다.");
        }
    }

    /* 게시글 조회 */
    @GetMapping("/board/{boardId}/vuln")
    public String getBoardForm_vuln(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);

        model.addAttribute("form", board);

        return "/board/getBoardForm_vuln";
    }

    @GetMapping("/board/{boardId}/secure")
    public String getBoardForm_secure(@PathVariable("boardId") Long boardId, Model model) {
        Board board = boardService.findBoard(boardId);

        model.addAttribute("form", board);

        return "/board/getBoardForm_secure";
    }

    /* 게시글 수정 */
    @GetMapping("/board/update/{boardId}/vuln")
    public String updateForm(@PathVariable("boardId") Long boardId, Model model) {

        Board board = boardService.findBoard(boardId);
        BoardForm boardForm = new BoardForm();

        boardForm.setId(board.getId());
        boardForm.setSubject(board.getSubject());
        boardForm.setContent(board.getContent());
        boardForm.setFileName(board.getFileName());
        boardForm.setFilePath(board.getFilePath());
        boardForm.setMember(board.getMember());

        model.addAttribute("form", boardForm);

        return "/board/updateBoardForm_vuln";

    }

    @PostMapping("/board/update/{boardId}/vuln")
    public String updateForm(@PathVariable("boardId") Long boardId, BoardForm form, Model model) {

        boardService.updateBoard(boardId,
                form.getSubject(),
                form.getContent(),
                form.getFileName(),
                form.getFilePath());

        model.addAttribute("message", "글 수정이 완료되었습니다.");
        model.addAttribute("searchUrl", "/board");

        return "message";

    }

    @GetMapping("/board/update/{boardId}/secure")
    public String updateForm_secure(HttpServletRequest request,
                             @PathVariable("boardId") Long boardId, Model model) {

        //게시물 수정 시 사용자 검증
        MemberInfo member = (MemberInfo) request.getSession().getAttribute("memberInfo");
        boardService.validateUpdate(member.getId(), boardId);

        Board board = boardService.findBoard(boardId);
        BoardForm boardForm = new BoardForm();

        boardForm.setId(board.getId());
        boardForm.setSubject(board.getSubject());
        boardForm.setContent(board.getContent());
        boardForm.setFileName(board.getFileName());
        boardForm.setFilePath(board.getFilePath());
        boardForm.setMember(board.getMember());

        model.addAttribute("form", boardForm);

        return "/board/updateBoardForm_secure";

    }

    @PostMapping("/board/update/{boardId}/secure")
    public String updateBoard_secure(HttpServletRequest request,
                              @PathVariable("boardId") Long boardId, BoardForm form, Model model) {

        String param = request.getParameter("_csrf");

        if (request.getSession().getAttribute("CSRF_TOKEN").equals(param)) {

            //게시물 수정 시 사용자 검증
            MemberInfo member = (MemberInfo) request.getSession().getAttribute("memberInfo");
            boardService.validateUpdate(member.getId(), boardId);

            boardService.updateBoard(boardId,
                    form.getSubject(),
                    form.getContent(),
                    form.getFileName(),
                    form.getFilePath());

            model.addAttribute("message", "글 수정이 완료되었습니다.");
            model.addAttribute("searchUrl", "/boards");

            return "message";
        } else {
            throw new PermissionException("권한이 없습니다.");
        }

    }

    /* 게시글 삭제 */
    @GetMapping("/board/delete/{boardId}/vuln")
    public String deleteBoard_vuln(@PathVariable("boardId") Long boardId, BoardForm form, HttpServletRequest request, Model model) {

        model.addAttribute("message", "삭제하시겠습니까?");
        boardService.deleteBoard(boardId);

        model.addAttribute("searchUrl", "/board");

        return "message";

    }

    @GetMapping("/board/delete/{boardId}/secure")
    public String deleteBoard_secure(@PathVariable("boardId") Long boardId, BoardForm form, HttpServletRequest request, Model model) {

        MemberInfo member = (MemberInfo) request.getSession().getAttribute("memberInfo");
        boardService.validateUpdate(member.getId(), boardId);


        model.addAttribute("message", "삭제하시겠습니까?");
        boardService.deleteBoard(boardId);

        model.addAttribute("searchUrl", "/board");


        return "message";

    }


}
