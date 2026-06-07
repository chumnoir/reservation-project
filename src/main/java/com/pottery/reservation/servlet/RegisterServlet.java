package com.pottery.reservation.servlet;

import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 会員登録(/register)を処理するServlet。
 * GET: 登録フォーム表示 / POST: 登録処理
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
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

        try {
            memberService.register(member);
            // 登録成功 → ログイン画面へ
            resp.sendRedirect(req.getContextPath() + "/login?registered=1");
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("member", member); // 入力値の保持
            req.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(req, resp);
        }
    }
}
