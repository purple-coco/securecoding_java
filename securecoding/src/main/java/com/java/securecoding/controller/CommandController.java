package com.java.securecoding.controller;

import com.java.securecoding.service.CommandService;
import com.java.securecoding.service.MemberService;
import com.java.securecoding.exception.PermissionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
public class CommandController {

    private static CommandService commandService;
    private final MemberService memberService;

    private static Map<Integer, String> URLMAP;


    static {
        URLMAP = new HashMap<>();
        URLMAP.put(1, "http://localhost:8089/");
        URLMAP.put(2,"http://localhost:8089/board/new");
        URLMAP.put(3,"http://localhost:8080/2/8");
    }


    @GetMapping("/1/1")
    public String SQLInjectionForm_code() {
        return "/1/1.1.code";
    }

    //2. 코드 삽입
    @GetMapping(value = {"/1/2", "/1/2/vuln", "/1/2/secure"})
    public String CommandForm(@RequestParam(required = false) String src) {
        return "/1/1.2";
    }

    @GetMapping("/1/2/code")
    public String CommandForm_code(){
        return "/1/1.2.code";
    }

    //2. 코드 삽입
    @PostMapping("/1/2/vuln")
    public String CommandForm_vuln(HttpServletRequest request, Model model) throws ScriptException {
        String src = request.getParameter("src");

        if (src == null) {
            return "/1/1.2";
        }
        String retValue = (String) commandService.EvalService(src);

        model.addAttribute("value", retValue);

        return "/1/1.2";
    }

    //2. 코드 삽입
    @PostMapping("/1/2/secure")
    public String CommandForm_secure(HttpServletRequest request, Model model) throws ScriptException {
        String src2 = request.getParameter("src2");

        if (src2 == null) {
            return "/1/1.2";
        }

        if (!src2.matches("[\\w]*")) {
            model.addAttribute("message", "허용하지 않는 문자열입니다.");
            model.addAttribute("searchUrl", "/1/2");

            return "message";
        } else {
            String retValue2 = (String) commandService.EvalService(src2);

            model.addAttribute("value2", retValue2);
        }

        return "/1/1.2";
    }

    //3. 경로 조작 및 자원 삽입
    @GetMapping(value = {"/1/3", "/1/3/vuln", "/1/3/secure"})
    public String PathForm(@RequestParam(required = false) String fileName) {
        return "/1/1.3";
    }

    @GetMapping("/1/3/code")
    public String PathForm_code() {
        return "1/1.3.code";
    }

    //3. 경로 조작 및 자원 삽입
    @PostMapping("/1/3/vuln")
    public String PathForm_vuln(HttpServletRequest request, Model model) throws IOException {

        String fileName = request.getParameter("fileName");
        String result;

        if (fileName == null) {
            return "/1/1.3";
        } else {
            result = commandService.filePathService(fileName);
            model.addAttribute("line", result);
        }

        return "/1/1.3";
    }

    //3. 경로 조작 및 자원 삽입
    @PostMapping("/1/3/secure")
    public String PathForm_secure(HttpServletRequest request, Model model) throws IOException {

        String fileName2 = request.getParameter("fileName2");
        String result2;

        if (fileName2 == null) {
            return "/1/1.3";
        }

        if (!fileName2.matches("[\\%\\.\\/]*")) { //./\\%
            model.addAttribute("message", "허용하지 않는 특수문자입니다.");
            model.addAttribute("searchUrl", "/1/3");

            return "message";
        } else {
            result2 = commandService.filePathService(fileName2);
            model.addAttribute("line2", result2);
        }
        return "/1/1.3";
    }

    //4. 크로스사이트 스크립트
    @GetMapping(value = {"/1/4", "/1/4/vuln", "/1/4/secure"})
    public String XSSForm(@RequestParam(required = false) String input) {
        return "/1/1.4";
    }

    @GetMapping("/1/4/code")
    public String XSSForm_code() {
        return "/1/1.4.code";
    }

    @PostMapping("/1/4/vuln")
    public String XSSForm_vuln(HttpServletRequest request, Model model) {
        String input = request.getParameter("input");

        model.addAttribute("input", input);
        model.addAttribute("result", input);
        
        return "/1/1.4";
    }

    @PostMapping("/1/4/secure")
    public String XSSForm_secure(HttpServletRequest request, Model model) {
        String input2 = request.getParameter("input2");
        String result2 = commandService.XSSFilterService(input2);

        model.addAttribute("result2", result2);

        return "/1/1.4";
    }

    //5. 운영체제 명령어 삽입
    @GetMapping(value = {"/1/5", "/1/5/vuln", "/1/5/secure"})
    public String OSCommand(@RequestParam(required = false) String cmd) {
        return "/1/1.5";
    }

    @GetMapping("/1/5/code")
    public String OSCommand_code() {
        return "1/1.5.code";
    }

    //5. 운영체제 명령어 삽입
    @PostMapping("/1/5/vuln")
    public String OSCommand_vuln(HttpServletRequest request, Model model) throws IOException {

        String cmd = request.getParameter("cmd");
        String result;

        if (cmd == null) {
            return "/1/1.5";
        } else {
            result = commandService.OSCommandService(cmd);
            model.addAttribute("line", result);
        }

        return "/1/1.5";//templates 경로
    }

    //5. 운영체제 명령어 삽입
    @PostMapping("/1/5/secure")
    public String OSCommand_secure(HttpServletRequest request, Model model) throws IOException {

        String cmd2 = request.getParameter("cmd2");
        String result2;

        List<String> allowedCommands = new ArrayList<String>();
        allowedCommands.add("cal");

        if (cmd2 == null) {
            return "/1/1.5";
        }

        if (allowedCommands.contains(cmd2)) {
            result2 = commandService.OSCommandService(cmd2);
            model.addAttribute("line2", result2);

        } else if (!cmd2.matches("[|;&:>-]*")) {
            model.addAttribute("message", "허용하지 않는 명령어입니다.");
            model.addAttribute("searchUrl", "/1/5");//url 경로

            return "message";
        }

        return "/1/1.5";//templates 경로
    }

    //6. 위험한 형식 파일 업로드
    @GetMapping("/1/6/code")
    public String FileUpload_code() {
        return "/1/1.6.code";
    }

    //7. 신뢰되지 않는 URL 주소로 자동접속 연결
    @GetMapping(value = {"/1/7", "/1/7/vuln", "/1/7/secure"})
    public String URLConnected(@RequestParam(required = false) String url) {
        return "/1/1.7";
    }

    @GetMapping("/1/7/code")
    public String URLConnected_code() {
        return "/1/1.7.code";
    }

    //7. 신뢰되지 않는 URL 주소로 자동접속 연결
    @PostMapping("/1/7/vuln")
    public String URLConnected_vuln(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        String url = request.getParameter("url");

        if (url == null) {
            return "/1/1.7";
        } else {
            response.sendRedirect("http://" + url);
        }

        return "/1/1.7";
    }

    //7. 신뢰되지 않는 URL 주소로 자동접속 연결
    @PostMapping("/1/7/secure")
    public String URLConnected_secure(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        String[] allowedUrl = {"/", "/1/vuln5"};

        String url2 = request.getParameter("url2");

        if (url2 == null) {
            return "/1/1.7";
        }
        try {
            url2 = allowedUrl[Integer.parseInt(url2)];
            response.sendRedirect(url2);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            model.addAttribute("message", "잘못된 접근입니다.");
            model.addAttribute("searchUrl", "/1/7");

            return "message";
        }

        return "/1/1.7";
    }

    //8. 부적절한 XML 외부 개체 참조
    @GetMapping(value = {"/1/8", "/1/8/vuln", "/1/8/secure"})
    public String XXEForm(@RequestParam(required = false) String filePath) {
        return "/1/1.8";
    }

    @GetMapping("/1/8/code")
    public String XXEForm_code(@RequestParam(required = false) String filePath) {
        return "/1/1.8.code";
    }


    //8. 부적절한 XML 외부 개체 참조
    @PostMapping("/1/8/vuln")
    public String XXEForm_vuln(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException, SAXException, ParserConfigurationException {

        String filepath = request.getParameter("filepath");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(filepath));

        Node node = document.getDocumentElement().getFirstChild();
        String result = node.getNodeValue();


        model.addAttribute("line", result);

        return "/1/1.8";
    }

    //8. 부적절한 XML 외부 개체 참조
    @PostMapping("/1/8/secure")
    public String XXEForm_secure(HttpServletRequest request, Model model) throws IOException, SAXException, ParserConfigurationException {
        String filepath2 = request.getParameter("filepath2");

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        // 외부 일반 엔티티를 포함하지 않도록 설정한다.
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        // 외부 파라미터도 포함하지 않도록 설정한다.
        documentBuilderFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        // 외부 DTD 비활성화한다.
        documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
        // XIncude를 사용하지 않는다.
        documentBuilderFactory.setXIncludeAware(false);
        // 생성된 파서가 엔티티 참조 노드를 확장하지 않도록 한다.
        documentBuilderFactory.setExpandEntityReferences(false);

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File(filepath2));

        Node node = document.getDocumentElement().getFirstChild();
        String result = node.getNodeValue();

        if (result == null) {
            throw new PermissionException("접근할 수 없습니다.");
        }

        model.addAttribute("line2", result);

        return "/1/1.8";
    }

    //9. XML 삽입
    @GetMapping(value = {"/1/9", "/1/9/vuln", "/1/9/secure"})
    public String XPathForm(@RequestParam(required = false) String word) {
        return "/1/1.9";
    }

    @GetMapping("/1/9/code")
    public String XPathForm_code(@RequestParam(required = false) String word) {
        return "/1/1.9.code";
    }

    //9. XML 삽입
    @PostMapping("/1/9/vuln")
    public String XPathForm_vuln(HttpServletRequest request, Model model) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        String word = request.getParameter("word");

        if(word == null) {
            return "/1/1.9";
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File("./tmp/user.xml"));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        XPathExpression expr = xPath.compile("//user[@name='"+ word + "']");//모든 user 요소

        Object result = expr.evaluate(document, XPathConstants.NODESET);

        NodeList nodes = (NodeList)result;
        String values = "";

        String[] value = new String[nodes.getLength()];
        for (int i = 0; i < nodes.getLength(); i++) {

            value[i] = nodes.item(i).getTextContent();
            values += "\n" + value[i];

            model.addAttribute("value", values);

        }
        return "/1/1.9";
    }

    //9. XML 삽입
    @PostMapping("/1/9/secure")
    public String XPathForm_secure(HttpServletRequest request, Model model) throws XPathExpressionException, IOException, SAXException, ParserConfigurationException {
        String word2 = request.getParameter("word2");

        if(word2 == null) {
            return "/1/1.9";
        }

        if (!word2.matches("[\\w]*")) {
            model.addAttribute("message", "허용하지 않는 문자열입니다.");
            model.addAttribute("searchUrl", "/1/9");

            return "message";
        }

        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(new File("./tmp/user.xml"));

        XPathFactory xPathFactory = XPathFactory.newInstance();
        XPath xPath = xPathFactory.newXPath();

        XPathExpression expr = xPath.compile("//user[@name='"+ word2 + "']");//모든 user 요소

        Object result = expr.evaluate(document, XPathConstants.NODESET);

        NodeList nodes = (NodeList)result;
        String values = "";

        String[] value = new String[nodes.getLength()];
        for (int i = 0; i < nodes.getLength(); i++) {

            value[i] = nodes.item(i).getTextContent();
            values += "\n" + value[i];

            model.addAttribute("value2", values);

        }

        return "/1/1.9";
    }

    //10. LDAP 삽입
    @GetMapping("/1/10")
    public String LDAPInjection_code() {
        return "/1/1.10";
    }

    //12. 서버사이드 요청 위조 - admin 페이지
    @GetMapping("/admin")
    public String AdminForm_vuln(@RequestParam(required = false) String username,
                                 @RequestParam(required = false) String password) {
        return "/admin";
    }

    //12. 서버사이드 요청 위조 - admin 페이지(접근 제한)
    @PostMapping("/admins")
    public String AdminForm_secure(HttpServletRequest request, Model model,
                                   @RequestParam(required = false) String username,
                                   @RequestParam(required = false) String password) {
        String ipv4 = request.getRemoteAddr();

        if(!ipv4.equals("127.0.0.1")) {
            model.addAttribute("message", "접근할 수 없습니다.");
            model.addAttribute("searchUrl", "/1/12");

            return "message";
        }

        return "/admin";
    }


    //12. 서버사이드 요청 위조
    @GetMapping(value = {"/1/12", "/1/12/vuln", "/1/12/secure"})
    public String SSRFForm(@RequestParam(required = false) String url) {
        return "/1/1.12";
    }

    @GetMapping("/1/12/code")
    public String SSRFForm_code() {
        return "/1/1.12.code";
    }

    //12. 서버사이드 요청 위조
    @PostMapping("/1/12/vuln")
    public String SSRFForm_vuln(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        String url = request.getParameter("url");

        if (url == null) {
            return "/1/1.12";
        } else {
            URL input_url = new URL(url);

            HttpURLConnection httpURLConnection = (HttpURLConnection) input_url.openConnection();
            httpURLConnection.setRequestMethod("GET");

            if (httpURLConnection.getResponseCode() == 200) {
                return "/admin";
            }
            model.addAttribute("url", url);
        }
        return "/1/1.12";
    }

    //12. 서버사이드 요청 위조
    @PostMapping("/1/12/secure")
    public String SSRFForm_secure(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {

        String url2 = request.getParameter("url2");

        if(url2 == null) {
            return "/1/1.12";
        }
        try {
            if (!isValidUrl(url2) && !isResponseCodeOk(url2)) {
                System.out.println("유효하지 않은 URL");
                model.addAttribute("message", "허용하지 않는 URL 입니다.");
                model.addAttribute("searchUrl", "/1/1.12");

                return "message";
            }

            // 성공 로직
            return getView(url2);
        } catch (NumberFormatException e) {
            model.addAttribute("message", "허용하지 않는 URL 입니다.");
            model.addAttribute("searchUrl", "/1/1.12");

            return "message";
        }

    }

    private boolean isValidUrl(String url) {

        return URLMAP.keySet().stream()
                .filter(key -> url.equals(String.valueOf(key)))
                .findAny().isPresent();
    }

    private boolean isResponseCodeOk(String url) {
        return getResponseCode(url) == HttpURLConnection.HTTP_OK;
    }

    private int getResponseCode(String url) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL inputUrl = new URL(URLMAP.get(Integer.parseInt(url)));
            httpURLConnection = (HttpURLConnection) inputUrl.openConnection();
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setRequestMethod("GET");
            return httpURLConnection.getResponseCode();

        } catch (IOException e) {
            System.out.println("error");
            throw new PermissionException("접근 권한이 없습니다.");
        }
    }

    private String getView(String url) {
        if (url.equals("1")) {
            return "redirect:/";
        }

        if (url.equals("2")) {
            return "/1/1.6";
        }

        return "/2/2.8";
    }

    //13. HTTP 응답분할
    @GetMapping("/1/13")
    public String HttpSplitForm() {
        return "/1/1.13";
    }

    //14. 정수형 오버플로우
    @GetMapping(value = {"/1/14", "/1/14/vuln", "/1/14/secure"})
    public String IntegerBuffForm(@RequestParam(required = false) String integer1,
                               @RequestParam(required = false) String integer2) {
        return "/1/1.14";
    }

    @GetMapping("/1/14/code")
    public String IntegerBuffForm_code() {
        return "/1/1.14.code";
    }

    @PostMapping("/1/14/vuln")
    public String IntegerBuffForm_vuln(HttpServletRequest request, HttpServletResponse response, Model model) throws NumberFormatException{
        String integer1 = request.getParameter("integer1");
        String integer2 = request.getParameter("integer2");

        if(integer1 == null || integer2 == null) {
            return "/1/1.14";
        }

        try {
            Integer int_integer1 = Integer.parseInt(integer1);
            Integer int_integer2 = Integer.parseInt(integer2);

            Integer int_total = int_integer1 * int_integer2;

            model.addAttribute("value", int_total);

        } catch (NumberFormatException e) {
            model.addAttribute("value", "Error");
        }
        return "/1/1.14";
    }

    @PostMapping("/1/14/secure")
    public String IntegerBuffForm_secure(HttpServletRequest request, HttpServletResponse response, Model model) {
        String integer3 = request.getParameter("integer3");
        String integer4 = request.getParameter("integer4");

        if(integer3 == null || integer4 == null) {
            return "/1/1.14";
        }

        BigInteger big_integer1 = new BigInteger(integer3);
        BigInteger big_integer2 = new BigInteger(integer4);

        BigInteger total = big_integer1.multiply(big_integer2);

        model.addAttribute("value2", total);

        return "/1/1.14";
    }

    //15. 보안기능 결정에 사용되는 부적절한 입력값
    @GetMapping("/1/15/code")
    public String deleteMemberForm_code(Model model) {
        model.addAttribute("members", memberService.findMembers());

        return "/1/1.15.code";
    }

    //16. 메모리 버퍼 오버플로우
    @GetMapping("/1/16")
    public String OverFlowForm_code() {
        return "/1/1.16";
    }

    //17. 포맷 스트링 삽입
    @GetMapping("/1/17")
    public String FormatStringForm_code() {
        return "/1/1.17";
    }
}
