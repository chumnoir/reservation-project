package com.pottery.reservation.combined.filter;

import com.pottery.reservation.dto.MemberDTO;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 【Service統合版】管理者向けページ(/combined/admin/*)のアクセス制御フィルタ。
 * 既存版 AdminAuthFilter と同じ方針:
 *   (1) IP許可リスト(ADMIN_ALLOWED_IPS)で店舗端末のみに限定し、許可外は404
 *   (2) 管理者ログイン(cbLoginAdmin)の確認
 * セッション属性は既存版と独立させるため cbLoginAdmin を使用する。
 */
@WebFilter("/combined/admin/*")
public class CombinedAdminAuthFilter implements Filter {

    private List<String> allowedIps;

    @Override
    public void init(FilterConfig config) {
        String raw = System.getProperty("ADMIN_ALLOWED_IPS");
        if (raw == null || raw.isBlank()) {
            raw = System.getenv("ADMIN_ALLOWED_IPS");
        }
        if (raw == null || raw.isBlank()) {
            raw = "127.0.0.1,::1,0:0:0:0:0:0:0:1";
        }
        allowedIps = Arrays.stream(raw.split(","))
                .map(String::trim).filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // (1) IP許可リスト。許可外は404で隠す。
        if (!isAllowedIp(req.getRemoteAddr())) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.equals("/combined/admin/login")) {
            chain.doFilter(request, response);
            return;
        }

        // (2) 管理者ログイン確認
        HttpSession session = req.getSession(false);
        MemberDTO loginAdmin = (session != null) ? (MemberDTO) session.getAttribute("cbLoginAdmin") : null;
        if (loginAdmin == null || !loginAdmin.isAdmin()) {
            res.sendRedirect(req.getContextPath() + "/combined/admin/login?error=session");
            return;
        }
        chain.doFilter(request, response);
    }

    private boolean isAllowedIp(String clientIp) {
        if (clientIp == null) return false;
        for (String p : allowedIps) {
            if (p.endsWith("*")) {
                if (clientIp.startsWith(p.substring(0, p.length() - 1))) return true;
            } else if (p.equals(clientIp)) {
                return true;
            }
        }
        return false;
    }
}
