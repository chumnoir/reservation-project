package controller.admin;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.ReservationDao;
import dao.WorkShopDao;
import dto.Reservation;
import dto.ReservationHistory;

/**
 * 予約管理（A03/A08-A09/A20-A21/A29-A30）。
 * 一覧（ラジオ選択）→ 編集 / 削除 → 確認 → 確定。
 *
 * 編集では代表者名と参加人数を変更でき、人数の増減に合わせて
 * 対象ワークショップの定員（残席数）を増減させる。
 * 削除では予約人数分の定員を戻す。
 */
@WebServlet("/admin/reservation/*")
public class AdminReservationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String path = request.getPathInfo();
		path = (path == null) ? "/" : path;
		try {
			switch (path) {
			case "/":               showList(request, response, null); break;
			case "/edit":           showEditForm(request, response); break;
			case "/delete/confirm": showDeleteConfirm(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/reservation");
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
			case "/edit/confirm":    showEditConfirm(request, response); break;
			case "/edit/complete":   doEditComplete(request, response); break;
			case "/delete/complete": doDeleteComplete(request, response); break;
			default: response.sendRedirect(request.getContextPath() + "/admin/reservation");
			}
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	private void showList(HttpServletRequest request, HttpServletResponse response, String error)
			throws ServletException, IOException {
		request.setAttribute("reservationList", new ReservationDao().getAllHistory());
		if (error != null) request.setAttribute("error", error);
		request.getRequestDispatcher("/jsp/admin/mng_reservation_list.jsp").forward(request, response);
	}

	private void dispatchFromList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		String id = request.getParameter("bookId");
		if (id == null || id.isEmpty()) {
			showList(request, response, "対象を1件選択してください。");
			return;
		}
		String ctx = request.getContextPath();
		if ("edit".equals(action)) {
			response.sendRedirect(ctx + "/admin/reservation/edit?id=" + id);
		} else if ("delete".equals(action)) {
			response.sendRedirect(ctx + "/admin/reservation/delete/confirm?id=" + id);
		} else {
			showList(request, response, null);
		}
	}

	private void showEditForm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long bookId = Long.parseLong(request.getParameter("id"));
		ReservationDao dao = new ReservationDao();
		Reservation r = dao.findByBookId(bookId);
		ReservationHistory h = dao.findHistoryByBookId(bookId);
		if (r == null || h == null) {
			response.sendRedirect(request.getContextPath() + "/admin/reservation");
			return;
		}
		request.setAttribute("reservation", r);
		request.setAttribute("history", h);
		request.getRequestDispatcher("/jsp/admin/mng_reservation_form.jsp").forward(request, response);
	}

	private void showEditConfirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long bookId = Long.parseLong(request.getParameter("id"));
		String guestName = trim(request.getParameter("guest_name"));
		String numStr = trim(request.getParameter("num_people"));

		String error = null;
		int num = 0;
		if (isBlank(guestName) || isBlank(numStr)) {
			error = "代表者名と参加人数を入力してください。";
		} else {
			try {
				num = Integer.parseInt(numStr);
				if (num <= 0) error = "参加人数は1以上で入力してください。";
			} catch (Exception e) {
				error = "参加人数は数値で入力してください。";
			}
		}

		if (error != null) {
			ReservationDao dao = new ReservationDao();
			request.setAttribute("reservation", dao.findByBookId(bookId));
			request.setAttribute("history", dao.findHistoryByBookId(bookId));
			request.setAttribute("error", error);
			request.getRequestDispatcher("/jsp/admin/mng_reservation_form.jsp").forward(request, response);
			return;
		}

		ReservationHistory h = new ReservationDao().findHistoryByBookId(bookId);
		request.setAttribute("history", h);
		request.setAttribute("id", String.valueOf(bookId));
		request.setAttribute("guest_name", guestName);
		request.setAttribute("num_people", num);
		request.getRequestDispatcher("/jsp/admin/mng_reservation_confirm.jsp").forward(request, response);
	}

	private void doEditComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long bookId = Long.parseLong(request.getParameter("id"));
		String guestName = trim(request.getParameter("guest_name"));
		int newNum = Integer.parseInt(request.getParameter("num_people"));

		ReservationDao dao = new ReservationDao();
		Reservation r = dao.findByBookId(bookId);
		if (r == null) {
			complete(request, response, false, "", "対象の予約が見つかりません。");
			return;
		}

		int workshopId = r.getWorkshop_id();
		int oldNum = r.getNum_people();
		int diff = newNum - oldNum;

		WorkShopDao wsDao = new WorkShopDao();

		// 人数が増える場合は、その差分だけ定員を確保できるか確認しながら減らす
		if (diff > 0) {
			boolean reduced = wsDao.decreaseCapacity(workshopId, diff);
			if (!reduced) {
				complete(request, response, false, "",
						"残席数が不足しているため、人数を増やせませんでした。");
				return;
			}
		} else if (diff < 0) {
			wsDao.increaseCapacity(workshopId, -diff);
		}

		boolean ok = dao.updateGuestAndNum(bookId, guestName, newNum);
		if (!ok && diff != 0) {
			// 更新に失敗したら定員の増減を巻き戻す
			if (diff > 0) {
				wsDao.increaseCapacity(workshopId, diff);
			} else {
				wsDao.decreaseCapacity(workshopId, -diff);
			}
		}
		complete(request, response, ok, "予約を更新しました。", "更新に失敗しました。");
	}

	private void showDeleteConfirm(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long bookId = Long.parseLong(request.getParameter("id"));
		ReservationHistory h = new ReservationDao().findHistoryByBookId(bookId);
		if (h == null) {
			response.sendRedirect(request.getContextPath() + "/admin/reservation");
			return;
		}
		request.setAttribute("history", h);
		request.getRequestDispatcher("/jsp/admin/mng_reservation_delete.jsp").forward(request, response);
	}

	private void doDeleteComplete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long bookId = Long.parseLong(request.getParameter("id"));
		ReservationDao dao = new ReservationDao();
		Reservation r = dao.findByBookId(bookId);

		boolean ok = dao.deleteByBookId(bookId);
		// 予約が消えた分、定員（残席）を戻す
		if (ok && r != null) {
			new WorkShopDao().increaseCapacity(r.getWorkshop_id(), r.getNum_people());
		}
		complete(request, response, ok, "予約を削除しました。", "削除に失敗しました。");
	}

	private void complete(HttpServletRequest request, HttpServletResponse response,
			boolean ok, String successMsg, String failMsg)
			throws ServletException, IOException {
		request.setAttribute("active", "reservation");
		request.setAttribute("success", ok);
		request.setAttribute("msg", ok ? successMsg : failMsg);
		request.setAttribute("backUrl", request.getContextPath() + "/admin/reservation");
		request.setAttribute("backLabel", "予約一覧へ戻る");
		request.getRequestDispatcher("/jsp/admin/mng_complete.jsp").forward(request, response);
	}

	private String trim(String v) { return v == null ? null : v.trim(); }
	private boolean isBlank(String v) { return v == null || v.isEmpty(); }
}
