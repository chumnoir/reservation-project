package com.pottery.reservation.util;

import java.util.regex.Pattern;

/**
 * 入力値バリデーション用のユーティリティ。
 *
 * 【動作の解説】
 * - 各 Service 層から呼び出し、形式チェックを共通化する。
 * - 正規表現は事前コンパイル(static final Pattern)して再利用し効率化する。
 * - 画面側(HTML5の required / pattern)でも一次チェックするが、
 *   ブラウザを経由しない不正リクエストに備えサーバ側でも必ず検証する
 *   (多層防御)。
 */
public class ValidationUtil {

    // 一般的なメール形式。厳密なRFC準拠ではなく実用上十分な簡易パターン。
    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");

    // 電話番号: 数字とハイフンのみ、10〜13文字(例 090-1234-5678 / 0312345678)
    private static final Pattern PHONE =
            Pattern.compile("^[0-9-]{10,13}$");

    private ValidationUtil() {
    }

    /** null または空白(空文字/スペースのみ)なら true */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /** 文字数が指定範囲内か(前後の空白は除いて判定) */
    public static boolean lengthBetween(String s, int min, int max) {
        if (s == null) return false;
        int len = s.trim().length();
        return len >= min && len <= max;
    }

    /** メールアドレス形式が正しいか */
    public static boolean isEmail(String s) {
        return s != null && EMAIL.matcher(s.trim()).matches();
    }

    /** 電話番号形式(数字・ハイフン)が正しいか */
    public static boolean isPhone(String s) {
        return s != null && PHONE.matcher(s.trim()).matches();
    }
}
