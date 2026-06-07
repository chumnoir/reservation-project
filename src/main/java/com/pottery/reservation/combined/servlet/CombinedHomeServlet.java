package com.pottery.reservation.combined.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 【Service統合版】入口(/combined)。
 * Service層を分離せず、各 Combined*Servlet が業務ロジックを直接持つ構成のトップ。
 */
@WebServlet(urlPatterns = {"/combined", "/combined/"})
public class CombinedHomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/combined/index.jsp").forward(req, resp);
    }
}
