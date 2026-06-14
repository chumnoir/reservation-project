package controller.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.CourseDao;
import dto.Course;

/**
 * コース管理（A05コース一覧 / コース追加・編集・削除）。
 * 一覧（ラジオ選択）→ 追加 / 編集 / 削除 → 確認 → 確定。
 */
@WebServlet("/admin/course/*")
public class AdminCourseServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		path = (path == null) ? "/" : path;
		try {
			switch (path) {
			case "/":               showList(request, response, null); break;
			case "/add":            showAddForm(request, response); break;
			case "/edit":           showEditForm(request, response); break;
			case "/delete/confirm": showDeleteConfirm(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/course");
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
			case "/add/confirm":     showConfirm(request, response, "add"); break;
			case "/add/complete":    doAddComplete(request, response); break;
			case "/edit/confirm":    showConfirm(request, response, "edit"); break;
			case "/edit/complete":   doEditComplete(request, response); break;
			case "/delete/complete": doDeleteComplete(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/course");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void showList(HttpServletRequest request, HttpServletResponse response, String error)
			throws ServletException, IOException {
		request.setAttribute("courseList", new CourseDao().getAll());
		if (error != null) request.setAttribute("error", error);
		request.getRequestDispatcher("/jsp/admin/mng_course_list.jsp").forward(request, response);
	}

	private void dispatchFromList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String id = request.getParameter("courseId");
		if (id == null || id.isEmpty()) {
			showList(request, response, "対象を1件選択してください。");
			return;
		}
		String ctx = request.getContextPath();
		if ("edit".equals(action)) {
			response.sendRedirect(ctx + "/admin/course/edit?id=" + id);
		} else if ("delete".equals(action)) {
			response.sendRedirect(ctx + "/admin/course/delete/confirm?id=" + id);
		} else {
			showList(request, response, null);
		}
	}

	private void showAddForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setAttribute("mode", "add");
		request.getRequestDispatcher("/jsp/admin/mng_course_form.jsp").forward(request, response);
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		Course course = new CourseDao().findById(id);
		if (course == null) {
			response.sendRedirect(request.getContextPath() + "/admin/course");
			return;
		}
		request.setAttribute("mode", "edit");
		request.setAttribute("course", course);
		request.getRequestDispatcher("/jsp/admin/mng_course_form.jsp").forward(request, response);
	}

	private void showConfirm(HttpServletRequest request, HttpServletResponse response, String mode)
			throws ServletException, IOException {
		String id = request.getParameter("id");
		String name = trim(request.getParameter("course_name"));
		String priceStr = trim(request.getParameter("price"));
		String timeStr = trim(request.getParameter("required_time"));
		String desc = request.getParameter("description");

		String error = validate(name, priceStr, timeStr);
		if (error != null) {
			Course c = new Course();
			if (id != null && !id.isEmpty()) c.setCourse_id(Long.parseLong(id));
			c.setCourse_name(name);
			c.setDescription(desc);
			try { c.setPrice(Integer.parseInt(priceStr)); } catch (Exception ignore) {}
			try { c.setRequired_time(Integer.parseInt(timeStr)); } catch (Exception ignore) {}
			request.setAttribute("mode", mode);
			request.setAttribute("error", error);
			request.setAttribute("course", c);
			request.getRequestDispatcher("/jsp/admin/mng_course_form.jsp").forward(request, response);
			return;
		}

		request.setAttribute("mode", mode);
		request.setAttribute("id", id);
		request.setAttribute("course_name", name);
		request.setAttribute("price", priceStr);
		request.setAttribute("required_time", timeStr);
		request.setAttribute("description", desc);
		request.getRequestDispatcher("/jsp/admin/mng_course_confirm.jsp").forward(request, response);
	}

	private void doAddComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Course c = buildFromRequest(request, false);
		boolean ok = new CourseDao().insert(c);
		complete(request, response, ok, "コースを追加しました。", "追加に失敗しました。");
	}

	private void doEditComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Course c = buildFromRequest(request, true);
		boolean ok = new CourseDao().update(c);
		complete(request, response, ok, "コースを更新しました。", "更新に失敗しました。");
	}

	private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		Course c = new CourseDao().findById(id);
		if (c == null) {
			response.sendRedirect(request.getContextPath() + "/admin/course");
			return;
		}
		request.setAttribute("course", c);
		request.getRequestDispatcher("/jsp/admin/mng_course_delete.jsp").forward(request, response);
	}

	private void doDeleteComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = Long.parseLong(request.getParameter("id"));
		boolean ok = new CourseDao().deleteById(id);
		String fail = "削除に失敗しました。このコースを使っているワークショップがあると削除できません。";
		complete(request, response, ok, "コースを削除しました。", fail);
	}

	private Course buildFromRequest(HttpServletRequest request, boolean withId) {
		Course c = new Course();
		if (withId) c.setCourse_id(Long.parseLong(request.getParameter("id")));
		c.setCourse_name(trim(request.getParameter("course_name")));
		c.setPrice(Integer.parseInt(request.getParameter("price")));
		c.setRequired_time(Integer.parseInt(request.getParameter("required_time")));
		c.setDescription(request.getParameter("description"));
		return c;
	}

	private void complete(HttpServletRequest request, HttpServletResponse response,
			boolean ok, String successMsg, String failMsg)
			throws ServletException, IOException {
		request.setAttribute("active", "course");
		request.setAttribute("success", ok);
		request.setAttribute("msg", ok ? successMsg : failMsg);
		request.setAttribute("backUrl", request.getContextPath() + "/admin/course");
		request.setAttribute("backLabel", "コース一覧へ戻る");
		request.getRequestDispatcher("/jsp/admin/mng_complete.jsp").forward(request, response);
	}

	private String validate(String name, String price, String time) {
		if (isBlank(name) || isBlank(price) || isBlank(time)) {
			return "コース名・参加費・所要時間を入力してください。";
		}
		try {
			if (Integer.parseInt(price) < 0 || Integer.parseInt(time) < 0) {
				return "参加費・所要時間は0以上で入力してください。";
			}
		} catch (Exception e) {
			return "参加費・所要時間は数値で入力してください。";
		}
		return null;
	}

	private String trim(String v) { return v == null ? null : v.trim(); }
	private boolean isBlank(String v) { return v == null || v.isEmpty(); }
}
