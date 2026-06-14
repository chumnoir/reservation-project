package controller.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDao;
import dto.User;

/**
 * 会員管理（A06/A16/A28/A37）。
 * 一覧（ラジオ選択）→ 削除確認 → 削除確定。フロー図に従い削除のみ。
 */
@WebServlet("/admin/member/*")
public class AdminMemberServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		path = (path == null) ? "/" : path;
		try {
			switch (path) {
			case "/":               showList(request, response, null); break;
			case "/delete/confirm": showDeleteConfirm(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/member");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String path = request.getPathInfo();
		path = (path == null) ? "/" : path;
		try {
			switch (path) {
			case "/":                dispatchFromList(request, response); break;
			case "/delete/complete": doDeleteComplete(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/member");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void showList(HttpServletRequest request, HttpServletResponse response, String error)
			throws ServletException, IOException {
		request.setAttribute("memberList", new UserDao().getAll());
		if (error != null) request.setAttribute("error", error);
		request.getRequestDispatcher("/jsp/admin/mng_member_list.jsp").forward(request, response);
	}

	private void dispatchFromList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String id = request.getParameter("userId");
		if (id == null || id.isEmpty()) {
			showList(request, response, "対象を1件選択してください。");
			return;
		}
		response.sendRedirect(request.getContextPath() + "/admin/member/delete/confirm?id=" + id);
	}

	private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		User user = new UserDao().findById(id);
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/admin/member");
			return;
		}
		request.setAttribute("member", user);
		request.getRequestDispatcher("/jsp/admin/mng_member_delete.jsp").forward(request, response);
	}

	private void doDeleteComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		boolean ok = new UserDao().deleteById(id);
		request.setAttribute("active", "member");
		request.setAttribute("success", ok);
		request.setAttribute("msg", ok ? "会員情報を削除しました。" : "削除に失敗しました。");
		request.setAttribute("backUrl", request.getContextPath() + "/admin/member");
		request.setAttribute("backLabel", "会員一覧へ戻る");
		request.getRequestDispatcher("/jsp/admin/mng_complete.jsp").forward(request, response);
	}
}
