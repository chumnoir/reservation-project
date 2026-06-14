package controller.admin;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CourseDao;
import dao.WorkShopDao;
import dto.Course;
import dto.WorkshopDisplay;

/**
 * ワークショップ管理（A05/A13/A15/A25-A27/A34-A36）。
 * 一覧（ラジオ選択）→ 追加 / 編集 / 削除 →（入力）→ 確認 → 確定。
 *
 * URL: /admin/workshop, /admin/workshop/add(/confirm|/complete),
 *      /admin/workshop/edit(/confirm|/complete), /admin/workshop/delete(/confirm|/complete)
 */
@WebServlet("/admin/workshop/*")
public class AdminWorkshopServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String path = request.getPathInfo();
		path = (path == null) ? "/" : path;

		try {
			switch (path) {
			case "/":
				showList(request, response, null);
				break;
			case "/add":
				showAddForm(request, response);
				break;
			case "/edit":
				showEditForm(request, response);
				break;
			case "/delete/confirm":
				showDeleteConfirm(request, response);
				break;
			default:
				response.sendRedirect(request.getContextPath() + "/admin/workshop");
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
			case "/":
				dispatchFromList(request, response);
				break;
			case "/add/confirm":
				showConfirm(request, response, "add");
				break;
			case "/add/complete":
				doAddComplete(request, response);
				break;
			case "/edit/confirm":
				showConfirm(request, response, "edit");
				break;
			case "/edit/complete":
				doEditComplete(request, response);
				break;
			case "/delete/complete":
				doDeleteComplete(request, response);
				break;
			default:
				response.sendRedirect(request.getContextPath() + "/admin/workshop");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/* ---------- 一覧 ---------- */

	private void showList(HttpServletRequest request, HttpServletResponse response, String error)
			throws ServletException, IOException {
		List<WorkshopDisplay> list = new WorkShopDao().findWorkshopDisplays();
		request.setAttribute("workshops", list);
		if (error != null) {
			request.setAttribute("error", error);
		}
		request.getRequestDispatcher("/jsp/admin/mng_workshop_list.jsp")
				.forward(request, response);
	}

	private void dispatchFromList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String id = request.getParameter("workshopId");
		if (id == null || id.isEmpty()) {
			showList(request, response, "対象を1件選択してください。");
			return;
		}
		String ctx = request.getContextPath();
		if ("edit".equals(action)) {
			response.sendRedirect(ctx + "/admin/workshop/edit?id=" + id);
		} else if ("delete".equals(action)) {
			response.sendRedirect(ctx + "/admin/workshop/delete/confirm?id=" + id);
		} else {
			showList(request, response, null);
		}
	}

	/* ---------- 追加・編集フォーム ---------- */

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("mode", "add");
		request.setAttribute("courses", new CourseDao().getAll());
		request.getRequestDispatcher("/jsp/admin/mng_workshop_form.jsp")
				.forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		WorkshopDisplay w = new WorkShopDao().findWorkshopDisplayById(id);
		if (w == null) {
			response.sendRedirect(request.getContextPath() + "/admin/workshop");
			return;
		}
		request.setAttribute("mode", "edit");
		request.setAttribute("workshop", w);
		request.setAttribute("courses", new CourseDao().getAll());
		request.getRequestDispatcher("/jsp/admin/mng_workshop_form.jsp")
				.forward(request, response);
	}

	/* ---------- 確認 ---------- */

	private void showConfirm(HttpServletRequest request, HttpServletResponse response, String mode)
			throws ServletException, IOException {

		String id = request.getParameter("id");
		String courseIdStr = trim(request.getParameter("courseId"));
		String dateStr = trim(request.getParameter("workshop_date"));
		String timeStr = trim(request.getParameter("start_time"));
		String capacityStr = trim(request.getParameter("capacity"));

		String error = validate(courseIdStr, dateStr, timeStr, capacityStr);
		if (error != null) {
			// フォームに戻す
			request.setAttribute("mode", mode);
			request.setAttribute("error", error);
			request.setAttribute("courses", new CourseDao().getAll());
			request.setAttribute("formCourseId", courseIdStr);
			request.setAttribute("formDate", dateStr);
			request.setAttribute("formTime", timeStr);
			request.setAttribute("formCapacity", capacityStr);
			if ("edit".equals(mode)) {
				WorkshopDisplay w = new WorkShopDao()
						.findWorkshopDisplayById(Long.parseLong(id));
				request.setAttribute("workshop", w);
			}
			request.getRequestDispatcher("/jsp/admin/mng_workshop_form.jsp")
					.forward(request, response);
			return;
		}

		Course course = new CourseDao().findById(Long.parseLong(courseIdStr));

		request.setAttribute("mode", mode);
		request.setAttribute("id", id);
		request.setAttribute("courseId", courseIdStr);
		request.setAttribute("courseName", course == null ? "" : course.getCourse_name());
		request.setAttribute("workshop_date", dateStr);
		request.setAttribute("start_time", timeStr);
		request.setAttribute("capacity", capacityStr);
		request.getRequestDispatcher("/jsp/admin/mng_workshop_confirm.jsp")
				.forward(request, response);
	}

	/* ---------- 確定 ---------- */

	private void doAddComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long courseId = Long.parseLong(request.getParameter("courseId"));
		LocalDate date = LocalDate.parse(request.getParameter("workshop_date"));
		LocalTime time = LocalTime.parse(request.getParameter("start_time"));
		int capacity = Integer.parseInt(request.getParameter("capacity"));

		boolean ok = new WorkShopDao().insertWorkshop(courseId, date, time, capacity);
		complete(request, response, ok,
				"ワークショップを追加しました。", "追加に失敗しました。");
	}

	private void doEditComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		long courseId = Long.parseLong(request.getParameter("courseId"));
		LocalDate date = LocalDate.parse(request.getParameter("workshop_date"));
		LocalTime time = LocalTime.parse(request.getParameter("start_time"));
		int capacity = Integer.parseInt(request.getParameter("capacity"));

		boolean ok = new WorkShopDao().updateWorkshop(id, courseId, date, time, capacity);
		complete(request, response, ok,
				"ワークショップを更新しました。", "更新に失敗しました。");
	}

	/* ---------- 削除 ---------- */

	private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		WorkshopDisplay w = new WorkShopDao().findWorkshopDisplayById(id);
		if (w == null) {
			response.sendRedirect(request.getContextPath() + "/admin/workshop");
			return;
		}
		request.setAttribute("workshop", w);
		request.getRequestDispatcher("/jsp/admin/mng_workshop_delete.jsp")
				.forward(request, response);
	}

	private void doDeleteComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		boolean ok = new WorkShopDao().deleteWorkshop(id);
		complete(request, response, ok,
				"ワークショップを削除しました。", "削除に失敗しました。");
	}

	/* ---------- 共通 ---------- */

	private void complete(HttpServletRequest request, HttpServletResponse response,
			boolean ok, String successMsg, String failMsg)
			throws ServletException, IOException {
		request.setAttribute("active", "workshop");
		request.setAttribute("success", ok);
		request.setAttribute("msg", ok ? successMsg : failMsg);
		request.setAttribute("backUrl", request.getContextPath() + "/admin/workshop");
		request.setAttribute("backLabel", "ワークショップ一覧へ戻る");
		request.getRequestDispatcher("/jsp/admin/mng_complete.jsp")
				.forward(request, response);
	}

	private String validate(String courseId, String date, String time, String capacity) {
		if (isBlank(courseId) || isBlank(date) || isBlank(time) || isBlank(capacity)) {
			return "すべての項目を入力してください。";
		}
		try {
			LocalDate.parse(date);
			LocalTime.parse(time);
			Long.parseLong(courseId);
			int cap = Integer.parseInt(capacity);
			if (cap < 0) {
				return "定員は0以上で入力してください。";
			}
		} catch (Exception e) {
			return "入力値の形式が正しくありません。";
		}
		return null;
	}

	private String trim(String v) {
		return v == null ? null : v.trim();
	}

	private boolean isBlank(String v) {
		return v == null || v.isEmpty();
	}
}
