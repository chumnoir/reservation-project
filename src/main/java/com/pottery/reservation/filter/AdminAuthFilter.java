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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 管理者向けページ(/admin/*)のアクセス制御フィルタ。
 *
 * 【2段階の防御】
 *  (1) IP許可リスト … 店舗端末など、許可されたIPアドレスからのアクセスのみ通す。
 *      許可外のIPには 404 を返し、管理画面の存在自体を隠す
 *      (一般利用者からは「そんなURLは無い」ように見える)。
 *  (2) 管理者ログイン … IPを通過した上で、ログイン(loginAdmin)済みでなければ
 *      管理者ログイン画面へ誘導する。/admin/login 自体は(1)だけ通過すればよい。
 *
 * 【許可IPの設定】
 *  許可リストは再コンパイル不要で変更できるよう、システムプロパティ
 *  または環境変数 ADMIN_ALLOWED_IPS (カンマ区切り) で指定する。
 *    例) -DADMIN_ALLOWED_IPS=192.168.11.50,192.168.11.51
 *    例) -DADMIN_ALLOWED_IPS=192.168.11.*        (末尾*で前方一致)
 *  未指定時はローカルループバック(127.0.0.1 / ::1)のみ許可する。
 *
 * 【注意 / トンネル経由について】
 *  cloudflared 等のトンネル経由のアクセスは、Tomcat からは 127.0.0.1 として
 *  見える。デフォルト(ループバック許可)のままトンネルを公開すると管理画面に
 *  到達できてしまうため、店舗端末のみに限定したい場合は ADMIN_ALLOWED_IPS に
 *  店舗端末の実IPを設定し、ループバックを含めない運用にすること。
 */
@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {

    /** 許可IP一覧(完全一致、または末尾"*"による前方一致)。 */
    private List<String> allowedIps;

    @Override
    public void init(jakarta.servlet.FilterConfig config) {
        String raw = System.getProperty("ADMIN_ALLOWED_IPS");
        if (raw == null || raw.isBlank()) {
            raw = System.getenv("ADMIN_ALLOWED_IPS");
        }
        if (raw == null || raw.isBlank()) {
            // 既定: ローカルループバックのみ(IPv4 / IPv6)
            raw = "127.0.0.1,::1,0:0:0:0:0:0:0:1";
        }
        allowedIps = Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // (1) IP許可リストによる入口制限。許可外は 404 で存在を隠す。
        String clientIp = req.getRemoteAddr();
        if (!isAllowedIp(clientIp)) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // 管理者ログイン処理自体は(認証は不要、IPだけ通過すればよい)
        if (path.equals("/admin/login")) {
            chain.doFilter(request, response);
            return;
        }

        // (2) 管理者ログイン確認
        HttpSession session = req.getSession(false);
        MemberDTO loginAdmin = (session != null) ? (MemberDTO) session.getAttribute("loginAdmin") : null;
        if (loginAdmin == null || !loginAdmin.isAdmin()) {
            res.sendRedirect(req.getContextPath() + "/admin/login?error=session");
            return;
        }

        chain.doFilter(request, response);
    }

    /** クライアントIPが許可リストにマッチするか(完全一致 or 末尾*前方一致)。 */
    private boolean isAllowedIp(String clientIp) {
        if (clientIp == null) {
            return false;
        }
        for (String pattern : allowedIps) {
            if (pattern.endsWith("*")) {
                String prefix = pattern.substring(0, pattern.length() - 1);
                if (clientIp.startsWith(prefix)) {
                    return true;
                }
            } else if (pattern.equals(clientIp)) {
                return true;
            }
        }
        return false;
    }
}
