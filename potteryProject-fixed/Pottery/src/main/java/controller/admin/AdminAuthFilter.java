package controller.admin;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dto.Admin;

/**
 * 管理者画面（/admin/*）のアクセス制御フィルタ。
 *
 * - ログイン画面（/admin/login）だけは未ログインでも通す。
 * - それ以外の /admin/* は、セッションに管理者（adminUser）が無ければ
 *   ログイン画面へリダイレクトする。
 *
 * これにより、一般ユーザー（ユーザー側でログインしただけの人）や
 * 未ログインの人は管理者画面のどのページにも入れない。
 */
@WebFilter("/admin/*")
public class AdminAuthFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		String path = uri.substring(contextPath.length());

		// ログイン画面・ログイン処理は認証不要で通す
		if (path.equals("/admin/login")) {
			chain.doFilter(req, res);
			return;
		}

		HttpSession session = request.getSession(false);
		Admin adminUser = (session == null)
				? null
				: (Admin) session.getAttribute("adminUser");

		if (adminUser == null) {
			// 未ログインの管理者アクセスはログイン画面へ
			response.sendRedirect(contextPath + "/admin/login");
			return;
		}

		chain.doFilter(req, res);
	}
}
