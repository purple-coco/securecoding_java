package com.java.securecoding.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthorityService {

    private static Key key = null;

    static {
        if (key == null) {
            KeyGenerator keyGenerator;
            try {
                keyGenerator = KeyGenerator.getInstance("DES");
                keyGenerator.init(56);
                key = keyGenerator.generateKey();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public static String WeakEncryptService(String plainText) {

        String DES = "";
        StringBuffer stringBuffer = null;

        try {
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plaintext = plainText.getBytes(StandardCharsets.UTF_8);
            byte[] ciphertext = cipher.doFinal(plaintext);

            stringBuffer = new StringBuffer(ciphertext.length * 2);
            for (int i = 0; i < ciphertext.length; i++) {
                String hex = "0" + Integer.toHexString(0xff & ciphertext[i]);
                stringBuffer.append(hex.substring(hex.length() - 2));
            }

            DES = stringBuffer.toString();
            return DES;

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
            System.out.println("e = " + e);
        }
        return null;
    }

    public static boolean SecurePasswordService(String password) {
        String passwordPolicy = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,16}$";

        Pattern pattern = Pattern.compile(passwordPolicy);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    public static String NonSaltService(String plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(plainText.getBytes("UTF-8"));
        StringBuffer hexString = new StringBuffer();

        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }

        return hexString.toString();
    }

    public static String SaltService(String plainText) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String salt = new String(Base64.getEncoder().encode(bytes));

        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(salt.getBytes(StandardCharsets.UTF_8));
        messageDigest.update(plainText.getBytes(StandardCharsets.UTF_8));

        return String.format("%64x", new BigInteger(1, messageDigest.digest()));
    }

}
