package controller.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.AdminDao;
import dto.Admin;

/**
 * 管理者ログイン（A01）。
 * tbl_admin を参照して認証し、成功すればセッションに adminUser を入れて
 * 管理者トップ（A02）へ遷移する。
 */
@WebServlet("/admin/login")
public class AdminLoginServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// すでにログイン済みならトップへ
		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("adminUser") != null) {
			response.sendRedirect(request.getContextPath() + "/admin/top");
			return;
		}

		request.getRequestDispatcher("/jsp/admin/mng_login.jsp")
				.forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");

		String email = request.getParameter("email");
		String password = request.getParameter("password");

		email = (email == null) ? "" : email.trim();
		password = (password == null) ? "" : password.trim();

		if (email.isEmpty() || password.isEmpty()) {
			request.setAttribute("error", "メールアドレスとパスワードを入力してください。");
			request.setAttribute("email", email);
			request.getRequestDispatcher("/jsp/admin/mng_login.jsp")
					.forward(request, response);
			return;
		}

		AdminDao dao = new AdminDao();
		Admin admin = dao.findByEmailAndPassword(email, password);

		if (admin == null) {
			request.setAttribute("error", "メールアドレスまたはパスワードが違います。");
			request.setAttribute("email", email);
			request.getRequestDispatcher("/jsp/admin/mng_login.jsp")
					.forward(request, response);
			return;
		}

		// セッション固定攻撃対策で既存セッションを破棄してから作り直す
		HttpSession oldSession = request.getSession(false);
		if (oldSession != null) {
			oldSession.invalidate();
		}
		HttpSession session = request.getSession(true);
		session.setAttribute("adminUser", admin);

		response.sendRedirect(request.getContextPath() + "/admin/top");
	}
}
