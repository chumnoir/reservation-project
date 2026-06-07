package com.pottery.reservation.filter;

import com.pottery.reservation.dto.MemberDTO;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 管理者向けページの認証フィルタ。
 * /admin/* へのアクセス時、管理者ログイン(loginAdmin)が無ければ管理者ログイン画面へ誘導する。
 * 管理者ログイン画面自体(/admin/login)は除外する。
 */
@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 管理者ログイン処理自体は認証対象外
        if (path.equals("/admin/login")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        MemberDTO loginAdmin = (session != null) ? (MemberDTO) session.getAttribute("loginAdmin") : null;

        if (loginAdmin == null || !loginAdmin.isAdmin()) {
            res.sendRedirect(req.getContextPath() + "/admin/login?error=session");
            return;
        }
        chain.doFilter(request, response);
    }
}
