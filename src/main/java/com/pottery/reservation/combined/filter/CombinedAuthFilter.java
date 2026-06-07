package com.pottery.reservation.combined.filter;

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
 * 【Service統合版】一般会員向けページ(/combined/user/*)の認証フィルタ。
 * 既存版と独立させるため、セッション属性は cbLoginMember を使用する。
 */
@WebFilter("/combined/user/*")
public class CombinedAuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        MemberDTO loginMember = (session != null) ? (MemberDTO) session.getAttribute("cbLoginMember") : null;

        if (loginMember == null) {
            res.sendRedirect(req.getContextPath() + "/combined/login?error=session");
            return;
        }
        chain.doFilter(request, response);
    }
}
