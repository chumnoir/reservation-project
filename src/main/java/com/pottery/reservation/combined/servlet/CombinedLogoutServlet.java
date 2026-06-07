package com.pottery.reservation.combined.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 【Service統合版】一般会員ログアウト(/combined/logout)。
 */
@WebServlet("/combined/logout")
public class CombinedLogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.removeAttribute("cbLoginMember");
        }
        resp.sendRedirect(req.getContextPath() + "/combined/login?loggedout=1");
    }
}
