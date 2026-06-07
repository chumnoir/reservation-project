package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.MemberDAO;
import com.pottery.reservation.dto.MemberDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 【Service統合版】会員管理(/combined/admin/member)。
 * 一覧表示と削除のみ。ログイン中の管理者自身は削除しない。
 */
@WebServlet("/combined/admin/member")
public class CombinedMemberAdminServlet extends HttpServlet {

    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("members", memberDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/memberManage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        MemberDTO loginAdmin = (MemberDTO) req.getSession().getAttribute("cbLoginAdmin");
        if ("delete".equals(req.getParameter("action"))) {
            int memberId = parseInt(req.getParameter("memberId"), -1);
            if (loginAdmin == null || loginAdmin.getMemberId() != memberId) {
                memberDAO.delete(memberId);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/combined/admin/member");
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }
}
