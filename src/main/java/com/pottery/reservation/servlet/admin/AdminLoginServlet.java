package com.pottery.reservation.servlet.admin;

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
 * 管理者ログイン(/admin/login)を処理するServlet。
 * 一般ユーザー画面とは別URLでアクセスする管理者専用の入口。
 * role='ADMIN' の会員のみログインを許可し、セッションに loginAdmin を格納する。
 */
@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loginAdmin") != null) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/admin/adminLogin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        MemberDTO member = memberService.login(email, password);
        if (member != null && member.isAdmin()) {
            HttpSession session = req.getSession(true);
            session.setAttribute("loginAdmin", member);
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            req.setAttribute("error", "管理者アカウントでログインしてください。");
            req.getRequestDispatcher("/WEB-INF/jsp/admin/adminLogin.jsp").forward(req, resp);
        }
    }
}
