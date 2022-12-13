package com.java.securecoding.controller;

import com.java.securecoding.service.AuthorityService;
import com.java.securecoding.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthorityController {

    private static AuthorityService authorityService;

    //3. 중요한 자원에 대한 잘못된 권한 설정
    @GetMapping("/2/3")
    public String ResourceForm() {
        return "/2/2.3";
    }

    @GetMapping(value = {"/2/4", "/2/4/vuln", "/2/4/secure"})
    public String WeakEncryptForm(@RequestParam(required = false) String plainText) {
        return "/2/2.4";
    }

    @GetMapping("/2/4/code")
    public String WeakEncryptForm_code() {
        return "/2/2.4.code";
    }

    //4. 취약한 암호화 알고리즘 사용
    @PostMapping("/2/4/vuln")
    public String WeakEncryptForm_vuln(HttpServletRequest request, Model model) {

        String plainText = request.getParameter("plainText");
        String DES;

        if(plainText == null) {
            return "/2/2.4";
        }

        DES = authorityService.WeakEncryptService(plainText);
        model.addAttribute("value", DES);

        return "/2/2.4";
    }

    @PostMapping("/2/4/secure")
    public String WeakEncryptForm_secure(HttpServletRequest request, Model model) throws Exception {

        String plainText2 = request.getParameter("plainText2");
        String AES;

        if(plainText2 == null) {
            return "/2/2.4";
        }

        AES256 aes256 = new AES256();
        AES = aes256.encrypt(plainText2);

        model.addAttribute("value2", AES);

        return "/2/2.4";
    }

    //5. 암호화되지 않은 중요정보
    @GetMapping("/2/5/code")
    public String SecretInfoForm_code() {
        return "/2/2.5.code";
    }

    //6. 하드코드된 중요정보
    @GetMapping("/2/6")
    public String SecretInfo2Form() {
        return "/2/2.6";
    }

    //7. 충분하지 않은 키 길이 사용
    @GetMapping("/2/7")
    public String KeyForm() {
        return "/2/2.7";
    }

    //8. 적절하지 않은 난수값 사용
    @GetMapping(value = {"/2/8", "/2/8/vuln", "/2/8/secure"})
    public String RandomForm(@RequestParam(required = false) Integer value) {
        return "/2/2.8";
    }

    @GetMapping("/2/8/code")
    public String RandomForm_code() {
        return "/2/2.8.code";
    }

    //8. 적절하지 않은 난수값 사용
    @PostMapping("/2/8/vuln")
    public String RandomForm_vuln(HttpServletRequest request, Model model) {
        String value = request.getParameter("value");

        if(value.equals("")) {
            return "/2/2.8";
        }

        int int_value = Integer.parseInt(value);
        int result;

        Random random = new Random(100);
        result = random.nextInt(int_value);

        model.addAttribute("result", result);

        return "/2/2.8";
    }

    //8. 적절하지 않은 난수값 사용
    @PostMapping("/2/8/secure")
    public String RandomForm_secure(HttpServletRequest request, Model model) throws NoSuchAlgorithmException {

        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        secureRandom.setSeed(secureRandom.generateSeed(128));
        String authKey = new String(messageDigest.digest((secureRandom.nextLong() + "").getBytes()));

        authKey = Base64.getEncoder().encodeToString(authKey.getBytes());

        model.addAttribute("result2", authKey);

        return "/2/2.8";
    }

    @GetMapping(value = {"/2/9", "/2/9/vuln", "/2/9/secure"})
    public String PasswordForm(@RequestParam(required = false) String username,
                                @RequestParam(required = false) String password ) {
        return "/2/2.9";
    }

    @GetMapping("/2/9/code")
    public String PasswordForm_code() {
        return "/2/2.9.code";
    }

    //9. 취약한 비밀번호 허용
    @PostMapping("/2/9/vuln")
    public String PasswordForm_vuln(HttpServletRequest request, Model model) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username == null || password == null) {
            return "/2/2.9";
        }

        model.addAttribute("message", "회원가입이 완료되었습니다.");
        model.addAttribute("searchUrl", "/2/9");

        return "message";
    }

    //9. 취약한 비밀번호 허용
    @PostMapping("/2/9/secure")
    public String PasswordForm_secure(HttpServletRequest request, Model model) throws Exception {

        String username2 = request.getParameter("username2");
        String password2 = request.getParameter("password2");

        if (username2 == null || password2 == null) {
            return "/2/2.9";
        }
        if (authorityService.SecurePasswordService(password2)) {
            model.addAttribute("message", "회원가입이 완료되었습니다.");

        } else {
            model.addAttribute("message", "비밀번호는 최소 8자 이상, 숫자,문자,특수문자가 혼용되어야 합니다.");
        }
        model.addAttribute("searchUrl", "/2/9");
        return "message";
    }

    //10. 부적절한 전자서명 확인
    @GetMapping("/2/10")
    public String SignForm() {
        return "/2/2.10";
    }

    //11. 부적절한 인증서 유효성 검증
    @GetMapping("/2/11")
    public String CertificateForm() {
        return "/2/2.11";
    }

    //12. 사용자 하드디스크에 저장되는 쿠키를 통한 정보노출
    @GetMapping("/2/12")
    public String CookieForm() {
        return "/2/2.12";
    }

    //13. 주석문 안에 포함된 시스템 주요정보
    @GetMapping("/2/13")
    public String CommentaryForm() {
        return "/2/2.13";
    }

    //14. 솔트 없이 일방향 해쉬함수 사용
    @GetMapping(value = {"/2/14", "/2/14/vuln", "/2/14/secure"})
    public String HashForm(@RequestParam(required = false) String plainText) {
        return "/2/2.14";
    }

    @GetMapping("/2/14/code")
    public String HashForm_code() {
        return "/2/2.14.code";
    }

    //14. 솔트 없이 일방향 해쉬함수 사용
    @PostMapping("/2/14/vuln")
    public String HashForm_vuln(HttpServletRequest request, Model model) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String plainText = request.getParameter("plainText");
        String result;

        if(plainText == null) {
            return "/2/2.14";
        } else {
            result = authorityService.NonSaltService(plainText);
            model.addAttribute("value", result);
        }
        return "/2/2.14";
    }

    //14. 솔트 없이 일방향 해쉬함수 사용
    @PostMapping("/2/14/secure")
    public String HashForm_secure(HttpServletRequest request, Model model) throws Exception {

        String plainText2 = request.getParameter("plainText2");
        String result2;

        if (plainText2 == null) {
            return "/2/2.14";
        } else {
            result2 = authorityService.SaltService(plainText2);
            model.addAttribute("value2", result2);
        }
        return "/2/2.14";
    }
}

//AES256
class AES256 {
    public static String algorithm = "AES/CBC/PKCS5Padding";
    private final String key = "123456789012345678901234567890";
    private final String iv = key.substring(0, 16);

    public String encrypt(String text) throws Exception {

        Cipher cipher = Cipher.getInstance(algorithm);
        SecretKeySpec secretKeySpec = new SecretKeySpec(iv.getBytes(StandardCharsets.UTF_8), "AES");
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

        byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);

    }

}
