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
 * 一般会員向けページの認証フィルタ。
 * /user/* へのアクセス時、ログイン済みでなければログイン画面へ誘導する。
 * ログイン情報はセッション(loginMember)に保持され、ログアウトまで維持される。
 */
@WebFilter("/user/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        MemberDTO loginMember = (session != null) ? (MemberDTO) session.getAttribute("loginMember") : null;

        if (loginMember == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=session");
            return;
        }
        chain.doFilter(request, response);
    }
}
