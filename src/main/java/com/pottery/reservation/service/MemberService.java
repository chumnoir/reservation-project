package com.pottery.reservation.service;

import com.pottery.reservation.dao.MemberDAO;
import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.util.PasswordUtil;
import com.pottery.reservation.util.ValidationUtil;

import java.util.List;

/**
 * 会員に関する業務ロジック(登録・認証・管理)を担当するService。
 */
public class MemberService {

    private final MemberDAO memberDAO = new MemberDAO();

    /**
     * 会員登録。パスワードはハッシュ化して保存する。
     * @return 登録成否
     * @throws IllegalArgumentException 入力不備・メール重複時
     */
    public boolean register(MemberDTO member) {
        validateForRegister(member);
        if (memberDAO.existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("このメールアドレスは既に登録されています。");
        }
        member.setPassword(PasswordUtil.hash(member.getPassword()));
        member.setRole("USER");
        return memberDAO.insert(member);
    }

    /**
     * ログイン認証。成功時は会員情報、失敗時はnullを返す。
     */
    public MemberDTO login(String email, String password) {
        if (email == null || password == null) {
            return null;
        }
        MemberDTO member = memberDAO.findByEmail(email.trim());
        if (member != null && PasswordUtil.matches(password, member.getPassword())) {
            return member;
        }
        return null;
    }

    /** 全会員取得(管理者用) */
    public List<MemberDTO> findAll() {
        return memberDAO.findAll();
    }

    /** ID検索 */
    public MemberDTO findById(int memberId) {
        return memberDAO.findById(memberId);
    }

    /** 会員削除(管理者用) */
    public boolean delete(int memberId) {
        return memberDAO.delete(memberId);
    }

    /**
     * 登録時の入力バリデーション。
     * 不正があれば最初に見つかった項目で IllegalArgumentException を送出し、
     * 呼び出し元(Servlet)が画面にメッセージを表示する。
     */
    private void validateForRegister(MemberDTO m) {
        // 氏名: 必須・1〜100文字
        if (ValidationUtil.isBlank(m.getName())) {
            throw new IllegalArgumentException("氏名を入力してください。");
        }
        if (!ValidationUtil.lengthBetween(m.getName(), 1, 100)) {
            throw new IllegalArgumentException("氏名は100文字以内で入力してください。");
        }
        // メール: 必須・形式チェック
        if (ValidationUtil.isBlank(m.getEmail())) {
            throw new IllegalArgumentException("メールアドレスを入力してください。");
        }
        if (!ValidationUtil.isEmail(m.getEmail())) {
            throw new IllegalArgumentException("メールアドレスの形式が正しくありません。");
        }
        // 電話番号: 必須・数字とハイフンのみ(10〜13文字)
        if (ValidationUtil.isBlank(m.getPhone())) {
            throw new IllegalArgumentException("電話番号を入力してください。");
        }
        if (!ValidationUtil.isPhone(m.getPhone())) {
            throw new IllegalArgumentException("電話番号は数字とハイフンで入力してください(例: 090-1234-5678)。");
        }
        // 住所: 必須・255文字以内
        if (ValidationUtil.isBlank(m.getAddress())) {
            throw new IllegalArgumentException("住所を入力してください。");
        }
        if (!ValidationUtil.lengthBetween(m.getAddress(), 1, 255)) {
            throw new IllegalArgumentException("住所は255文字以内で入力してください。");
        }
        // パスワード: 6文字以上(空白のみは不可)
        if (ValidationUtil.isBlank(m.getPassword()) || m.getPassword().length() < 6) {
            throw new IllegalArgumentException("パスワードは6文字以上で入力してください。");
        }
    }
}
