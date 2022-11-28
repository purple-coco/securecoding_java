package com.java.securecoding.controller;

import com.java.securecoding.service.AuthorityService;
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
@Controller
public class AuthorityController {

    @GetMapping(value = {"/2/4", "/2/4/vuln", "/2/4/secure"})
    public String WeakEncryptForm(@RequestParam(required = false) String plainText) {

        return "/2/2.4";
    }

    //4. 취약한 암호화 알고리즘 사용
    @PostMapping("/2/4/vuln")
    public String WeakEncryptForm_vuln(HttpServletRequest request, Model model) {

        String plainText = request.getParameter("plainText");
        String DES;

        if(plainText == null) {
            return "/2/2.4";
        }

        DES = AuthorityService.WeakEncryptService(plainText);
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

    //7. 충분하지 않은 키 길이 사용
    @GetMapping("/2/7")
    public String KeyForm(@RequestParam(required = false) Integer value) {
        return "/2/2.7";
    }

    //8. 적절하지 않은 난수값 사용
    @GetMapping(value = {"/2/8", "/2/8/vuln", "/2/8/secure"})
    public String RandomForm(@RequestParam(required = false) Integer value) {
        return "/2/2.8";
    }

    //8. 적절하지 않은 난수값 사용
    @PostMapping("/2/8/vuln")
    public String RandomForm_vuln(HttpServletRequest request, Model model) {
        String value = request.getParameter("value");
        int int_value = Integer.parseInt(value);
        int result;

        Random random = new Random(100);
        result = random.nextInt(int_value);

        model.addAttribute("result", result);

        return "/2/2.8";
    }

    //8. 적절하지 않은 난수값 사용
    @PostMapping("/2/8/secure")
    public String RandomForm_secure(HttpServletRequest request, @RequestParam(required = false) String value2, Model model) throws NoSuchAlgorithmException {

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
        if (AuthorityService.SecurePasswordService(password2)) {
            model.addAttribute("message", "회원가입이 완료되었습니다.");

        } else {
            model.addAttribute("message", "최소 8자 이상, 숫자,문자,특수문자가 혼용되어야 합니다.");

        }
        model.addAttribute("searchUrl", "/2/9");
        return "message";
    }

    @GetMapping(value = {"/2/14", "/2/14/vuln", "/2/14/secure"})
    public String HashForm(@RequestParam(required = false) String plainText) {
        return "/2/2.14";
    }

    //14. 솔트 없이 일방향 해쉬함수 사용
    @PostMapping("/2/14/vuln")
    public String HashForm_vuln(HttpServletRequest request, Model model) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String plainText = request.getParameter("plainText");
        String result;

        if(plainText == null) {
            return "/2/2.14";
        } else {
            result = AuthorityService.NonSaltService(plainText);
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
            result2 = AuthorityService.SaltService(plainText2);
            model.addAttribute("value2", result2);
        }
        return "/2/2.14";
    }

    //16. 반복된 인증시도 제한 기능 부재
    @GetMapping(value = {"/2/16", "/2/16/vuln", "/2/16/secure"})
    public String LoginForm(@RequestParam(required = false) String username,
                            @RequestParam(required = false) String password) {
        return "/2/2.16";
    }

    //16. 반복된 인증시도 제한 기능 부재
    @PostMapping("/2/16/vuln")
    public String LoginForm_vuln(HttpServletRequest request, Model model) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if(username == null || password == null) {
            return "/2/2.16";
        } else {
            model.addAttribute("value", password);
        }
        return "/2/2.16";
    }

    //16. 반복된 인증시도 제한 기능 부재
    @PostMapping("/2/16/secure")
    public String LoginForm_secure(HttpServletRequest request, Model model) throws Exception {

        String username2 = request.getParameter("username2");
        String password2 = request.getParameter("password2");

        if (username2 == null || password2 == null) {
            return "/2/2.16";
        } else {
            if (AuthorityService.SecurePasswordService(password2)) {
                model.addAttribute("message", "회원가입이 완료되었습니다.");
            } else {
                model.addAttribute("message", "최소 8자 이상, 숫자,문자,특수문자가 혼용되어야 합니다.");
            }
            model.addAttribute("searchUrl", "/2/16");
            return "message";
        }
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
