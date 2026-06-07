package com.pottery.reservation.servlet.admin;

import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 会員管理(/admin/member)を処理するServlet。
 * 会員情報の一覧表示と削除のみを提供する(追加・編集は持たない)。
 */
@WebServlet("/admin/member")
public class MemberAdminServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("members", memberService.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/admin/memberManage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        MemberDTO loginAdmin = (MemberDTO) req.getSession().getAttribute("loginAdmin");

        if ("delete".equals(action)) {
            int memberId = parseInt(req.getParameter("memberId"), -1);
            // 自分自身(ログイン中の管理者)は削除させない
            if (loginAdmin == null || loginAdmin.getMemberId() != memberId) {
                memberService.delete(memberId);
            }
        }
        resp.sendRedirect(req.getContextPath() + "/admin/member");
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
