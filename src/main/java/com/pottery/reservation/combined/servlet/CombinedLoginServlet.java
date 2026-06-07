package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.MemberDAO;
import com.pottery.reservation.dto.MemberDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 【Service統合版】一般会員ログイン(/combined/login)。
 * 認証ロジック(メール検索＋平文パスワード比較)を doPost に直接記述する。
 * セッション属性は cbLoginMember。
 */
@WebServlet("/combined/login")
public class CombinedLoginServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("cbLoginMember") != null) {
            resp.sendRedirect(req.getContextPath() + "/combined/user/main");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/combined/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        MemberDTO member = (email == null) ? null : memberDAO.findByEmail(email.trim());
        // 平文パスワードを直接比較
        if (member != null && password != null && password.equals(member.getPassword())) {
            HttpSession session = req.getSession(true);
            session.setAttribute("cbLoginMember", member);
            resp.sendRedirect(req.getContextPath() + "/combined/user/main");
        } else {
            req.setAttribute("error", "メールアドレスまたはパスワードが正しくありません。");
            req.getRequestDispatcher("/WEB-INF/jsp/combined/login.jsp").forward(req, resp);
        }
    }
}
