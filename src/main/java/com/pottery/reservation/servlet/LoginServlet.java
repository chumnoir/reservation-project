package com.pottery.reservation.servlet;

import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 一般会員のログイン(/login)を処理するServlet。
 * 認証はメールアドレス + パスワードで行う。
 * 成功時はセッションに loginMember を格納し、メインページへ遷移する。
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // 既にログイン済みならメインへ
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loginMember") != null) {
            resp.sendRedirect(req.getContextPath() + "/user/main");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        MemberDTO member = memberService.login(email, password);
        if (member != null) {
            // ログイン情報をセッションに保持(ログアウトまで維持)
            HttpSession session = req.getSession(true);
            session.setAttribute("loginMember", member);
            resp.sendRedirect(req.getContextPath() + "/user/main");
        } else {
            req.setAttribute("error", "メールアドレスまたはパスワードが正しくありません。");
            req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
        }
    }
}
