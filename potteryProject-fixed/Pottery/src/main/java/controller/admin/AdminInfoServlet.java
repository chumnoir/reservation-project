package controller.admin;

import java.io.IOException;
import java.time.LocalDate;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.InfoDao;
import dto.Info;

/**
 * お知らせ管理（A04/A10-A12/A22-A24/A31-A33）。
 * 一覧（ラジオ選択）→ 追加 / 編集 / 削除 → 確認 → 確定。
 */
@WebServlet("/admin/info/*")
public class AdminInfoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		path = (path == null) ? "/" : path;
		try {
			switch (path) {
			case "/":              showList(request, response, null); break;
			case "/add":           showAddForm(request, response); break;
			case "/edit":          showEditForm(request, response); break;
			case "/delete/confirm": showDeleteConfirm(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/info");
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
			case "/":               dispatchFromList(request, response); break;
			case "/add/confirm":    showConfirm(request, response, "add"); break;
			case "/add/complete":   doAddComplete(request, response); break;
			case "/edit/confirm":   showConfirm(request, response, "edit"); break;
			case "/edit/complete":  doEditComplete(request, response); break;
			case "/delete/complete": doDeleteComplete(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/info");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void showList(HttpServletRequest request, HttpServletResponse response, String error)
			throws ServletException, IOException {
		request.setAttribute("infoList", new InfoDao().findAll());
		if (error != null) request.setAttribute("error", error);
		request.getRequestDispatcher("/jsp/admin/mng_info_list.jsp").forward(request, response);
	}

	private void dispatchFromList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String id = request.getParameter("infoId");
		if (id == null || id.isEmpty()) {
			showList(request, response, "対象を1件選択してください。");
			return;
		}
		String ctx = request.getContextPath();
		if ("edit".equals(action)) {
			response.sendRedirect(ctx + "/admin/info/edit?id=" + id);
		} else if ("delete".equals(action)) {
			response.sendRedirect(ctx + "/admin/info/delete/confirm?id=" + id);
		} else {
			showList(request, response, null);
		}
	}

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("mode", "add");
		request.getRequestDispatcher("/jsp/admin/mng_info_form.jsp").forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		Info info = new InfoDao().findById(id);
		if (info == null) {
			response.sendRedirect(request.getContextPath() + "/admin/info");
			return;
		}
		request.setAttribute("mode", "edit");
		request.setAttribute("info", info);
		request.getRequestDispatcher("/jsp/admin/mng_info_form.jsp").forward(request, response);
	}

	private void showConfirm(HttpServletRequest request, HttpServletResponse response, String mode)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String title = trim(request.getParameter("title"));
		String content = request.getParameter("content");
		String dateStr = trim(request.getParameter("post_date"));

		String error = null;
		if (isBlank(title) || content == null || content.trim().isEmpty()) {
			error = "タイトルとメッセージを入力してください。";
		} else if (!isBlank(dateStr)) {
			try { LocalDate.parse(dateStr); } catch (Exception e) { error = "日付の形式が正しくありません。"; }
		}

		if (error != null) {
			request.setAttribute("mode", mode);
			request.setAttribute("error", error);
			Info info = new Info();
			if (id != null && !id.isEmpty()) info.setInfo_id(Long.parseLong(id));
			info.setTitle(title);
			info.setContent(content);
			request.setAttribute("info", info);
			request.setAttribute("formDate", dateStr);
			request.getRequestDispatcher("/jsp/admin/mng_info_form.jsp").forward(request, response);
			return;
		}

		request.setAttribute("mode", mode);
		request.setAttribute("id", id);
		request.setAttribute("title", title);
		request.setAttribute("content", content);
		request.setAttribute("post_date", dateStr);
		request.getRequestDispatcher("/jsp/admin/mng_info_confirm.jsp").forward(request, response);
	}

	private void doAddComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Info info = buildFromRequest(request, false);
		boolean ok = new InfoDao().insert(info);
		complete(request, response, ok, "お知らせを追加しました。", "追加に失敗しました。");
	}

	private void doEditComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Info info = buildFromRequest(request, true);
		boolean ok = new InfoDao().update(info);
		complete(request, response, ok, "お知らせを更新しました。", "更新に失敗しました。");
	}

	private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		Info info = new InfoDao().findById(id);
		if (info == null) {
			response.sendRedirect(request.getContextPath() + "/admin/info");
			return;
		}
		request.setAttribute("info", info);
		request.getRequestDispatcher("/jsp/admin/mng_info_delete.jsp").forward(request, response);
	}

	private void doDeleteComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		boolean ok = new InfoDao().deleteById(id);
		complete(request, response, ok, "お知らせを削除しました。", "削除に失敗しました。");
	}

	private Info buildFromRequest(HttpServletRequest request, boolean withId) {
		Info info = new Info();
		if (withId) info.setInfo_id(Long.parseLong(request.getParameter("id")));
		info.setTitle(trim(request.getParameter("title")));
		info.setContent(request.getParameter("content"));
		String dateStr = trim(request.getParameter("post_date"));
		info.setPost_date(isBlank(dateStr) ? LocalDate.now() : LocalDate.parse(dateStr));
		return info;
	}

	private void complete(HttpServletRequest request, HttpServletResponse response,
			boolean ok, String successMsg, String failMsg)
			throws ServletException, IOException {
		request.setAttribute("active", "info");
		request.setAttribute("success", ok);
		request.setAttribute("msg", ok ? successMsg : failMsg);
		request.setAttribute("backUrl", request.getContextPath() + "/admin/info");
		request.setAttribute("backLabel", "お知らせ一覧へ戻る");
		request.getRequestDispatcher("/jsp/admin/mng_complete.jsp").forward(request, response);
	}

	private String trim(String v) { return v == null ? null : v.trim(); }
	private boolean isBlank(String v) { return v == null || v.isEmpty(); }
}
