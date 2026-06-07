package com.pottery.reservation.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * パスワードのハッシュ化ユーティリティ。
 * 学習用途のため SHA-256 を使用。実運用では bcrypt 等の利用を推奨。
 */
public class PasswordUtil {

    private PasswordUtil() {
    }

    /**
     * 平文パスワードをSHA-256でハッシュ化して16進文字列で返す。
     */
    public static String hash(String plain) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(plain.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("ハッシュ化に失敗しました", e);
        }
    }

    /**
     * 平文パスワードとハッシュ値が一致するか検証する。
     */
    public static boolean matches(String plain, String hashed) {
        return hash(plain).equals(hashed);
    }
}
