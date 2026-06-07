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
 * 【Service統合版】管理者ログイン(/combined/admin/login)。
 * role=ADMIN の会員のみ許可し、セッション cbLoginAdmin に格納する。
 */
@WebServlet("/combined/admin/login")
public class CombinedAdminLoginServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("cbLoginAdmin") != null) {
            resp.sendRedirect(req.getContextPath() + "/combined/admin/dashboard");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/adminLogin.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        MemberDTO member = (email == null) ? null : memberDAO.findByEmail(email.trim());
        if (member != null && member.isAdmin() && password != null && password.equals(member.getPassword())) {
            HttpSession session = req.getSession(true);
            session.setAttribute("cbLoginAdmin", member);
            resp.sendRedirect(req.getContextPath() + "/combined/admin/dashboard");
        } else {
            req.setAttribute("error", "管理者アカウントでログインしてください。");
            req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/adminLogin.jsp").forward(req, resp);
        }
    }
}
