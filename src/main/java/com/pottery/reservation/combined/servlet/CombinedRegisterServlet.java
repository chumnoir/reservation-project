package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.MemberDAO;
import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 【Service統合版】会員登録(/combined/register)。
 *
 * 既存版では MemberService が担っていた「入力検証・重複チェック・登録」を、
 * このServletの doPost 内に直接記述する(Service層を持たない構成)。
 * DAO/DTO/ValidationUtil は共通の部品として再利用する。
 */
@WebServlet("/combined/register")
public class CombinedRegisterServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/combined/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MemberDTO member = new MemberDTO();
        member.setName(req.getParameter("name"));
        member.setEmail(req.getParameter("email"));
        member.setPhone(req.getParameter("phone"));
        member.setAddress(req.getParameter("address"));
        member.setPassword(req.getParameter("password"));

        // --- 入力検証(旧 MemberService.validateForRegister 相当をインライン化) ---
        String error = validate(member);
        if (error == null && memberDAO.existsByEmail(member.getEmail())) {
            error = "このメールアドレスは既に登録されています。";
        }
        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("member", member);
            req.getRequestDispatcher("/WEB-INF/jsp/combined/register.jsp").forward(req, resp);
            return;
        }

        // --- 登録(パスワードは平文のまま) ---
        member.setRole("USER");
        memberDAO.insert(member);
        resp.sendRedirect(req.getContextPath() + "/combined/login?registered=1");
    }

    /** 入力検証。問題があればエラーメッセージを返す(なければ null)。 */
    private String validate(MemberDTO m) {
        if (ValidationUtil.isBlank(m.getName()) || !ValidationUtil.lengthBetween(m.getName(), 1, 100)) {
            return "氏名は100文字以内で入力してください。";
        }
        if (!ValidationUtil.isEmail(m.getEmail())) {
            return "メールアドレスの形式が正しくありません。";
        }
        if (!ValidationUtil.isPhone(m.getPhone())) {
            return "電話番号は数字とハイフンで入力してください(例: 090-1234-5678)。";
        }
        if (ValidationUtil.isBlank(m.getAddress()) || !ValidationUtil.lengthBetween(m.getAddress(), 1, 255)) {
            return "住所は255文字以内で入力してください。";
        }
        if (ValidationUtil.isBlank(m.getPassword()) || m.getPassword().length() < 6) {
            return "パスワードは6文字以上で入力してください。";
        }
        return null;
    }
}
