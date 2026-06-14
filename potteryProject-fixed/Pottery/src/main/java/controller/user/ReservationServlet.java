package controller.user;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.WorkShopDao;
import dto.Course;
import dto.User;

/**
 * 予約入力画面（コース選択）サーブレット。
 *
 * - doGet  : ワークショップ一覧の「予約」リンクなどから来たときに、
 *            予約入力画面（reservation.jsp）を表示する。
 * - doPost : 入力内容を検証して、確認画面（reservationConfirm.jsp）へ進む。
 *
 * 【改修】未ログインのまま予約画面に入れないよう、
 * doGet / doPost の冒頭でログインチェックを行い、
 * 未ログインの場合はログイン画面へ誘導する。
 * これにより、未ログインユーザーは予約確認画面まで到達できない。
 */
@WebServlet("/user/reservation")
public class ReservationServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 未ログインならログイン画面へ（ログイン後は元の予約画面に戻す）
		if (redirectToLoginIfNotAuthenticated(request, response)) {
			return;
		}

		try {
			String dateStr = trim(request.getParameter("date"));
			String timeStr = trim(request.getParameter("time"));
			String courseIdStr = trim(request.getParameter("courseId"));

			loadReservationScreenData(request, dateStr, timeStr, courseIdStr);

			request.getRequestDispatcher("/jsp/user/reservation.jsp")
					.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			throw new ServletException(e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 未ログインならログイン画面へ（確認画面に進ませない）
		if (redirectToLoginIfNotAuthenticated(request, response)) {
			return;
		}

		try {
			request.setCharacterEncoding("UTF-8");

			String dateStr = trim(request.getParameter("date"));
			String timeStr = trim(request.getParameter("time"));
			String courseIdStr = trim(request.getParameter("courseId"));
			String name = trim(request.getParameter("name"));
			String numStr = trim(request.getParameter("num"));

			if (isBlank(dateStr)
					|| isBlank(timeStr)
					|| isBlank(courseIdStr)
					|| isBlank(name)
					|| isBlank(numStr)) {

				forwardWithError(request, response,
						"入力漏れがあります。もう一度やり直してください。",
						dateStr, timeStr, courseIdStr);
				return;
			}

			LocalDate date;
			LocalTime time;
			Long courseId;
			int num;

			try {
				date = LocalDate.parse(dateStr);
				time = LocalTime.parse(timeStr);
				courseId = Long.parseLong(courseIdStr);
				num = Integer.parseInt(numStr);
			} catch (DateTimeParseException | NumberFormatException e) {
				forwardWithError(request, response,
						"入力値の形式が正しくありません。",
						dateStr, timeStr, courseIdStr);
				return;
			}

			if (num <= 0) {
				forwardWithError(request, response,
						"人数は1以上を入力してください。",
						dateStr, timeStr, courseIdStr);
				return;
			}

			WorkShopDao dao = new WorkShopDao();
			Integer workshopId = dao.findWorkshopId(date, time, courseId);

			if (workshopId == null) {
				forwardWithError(request, response,
						"予約対象が見つかりません。日付・時間・コースを再確認してください。",
						dateStr, timeStr, courseIdStr);
				return;
			}

			String courseName = "";
			List<Course> courseList = dao.findCoursesByDateAndTime(date, time);
			for (Course course : courseList) {
				if (course == null
						|| !String.valueOf(course.getCourse_id()).equals(courseIdStr)) {
					continue;
				}
				courseName = course.getCourse_name();
				break;
			}

			request.setAttribute("date", dateStr);
			request.setAttribute("time", timeStr);
			request.setAttribute("courseId", courseIdStr);
			request.setAttribute("courseName", courseName);
			request.setAttribute("name", name);
			request.setAttribute("num", numStr);

			request.getRequestDispatcher("/jsp/user/reservationConfirm.jsp")
					.forward(request, response);

		} catch (Exception e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath() + "/jsp/user/exception.jsp");
		}
	}

	/**
	 * ログイン状態を確認し、未ログインならログイン画面へリダイレクトする。
	 *
	 * ログイン後は元の予約画面（クエリ文字列込み）へ戻れるように、
	 * redirect パラメータに遷移元のパスを付けてログイン画面へ送る。
	 *
	 * @return リダイレクトを行った場合は true（呼び出し側は処理を中断する）
	 */
	private boolean redirectToLoginIfNotAuthenticated(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		HttpSession session = request.getSession(false);
		User loginUser = (session == null)
				? null
				: (User) session.getAttribute("loginUser");

		if (loginUser != null && loginUser.getUser_id() != null) {
			return false;
		}

		// 遷移元（このリクエスト）のパス + クエリ文字列を組み立てる
		String target = "/user/reservation";
		String queryString = request.getQueryString();
		if (queryString != null && !queryString.isEmpty()) {
			target = target + "?" + queryString;
		}

		String encodedTarget = URLEncoder.encode(target, StandardCharsets.UTF_8);

		response.sendRedirect(request.getContextPath()
				+ "/user/login?redirect=" + encodedTarget);

		return true;
	}

	private void loadReservationScreenData(HttpServletRequest request,
			String dateStr, String timeStr, String courseIdStr) throws Exception {

		WorkShopDao dao = new WorkShopDao();

		Set<LocalDate> dateSet = dao.findAvailableDates();
		LinkedHashSet<String> availableDateSet = new LinkedHashSet<>();
		for (LocalDate date : dateSet) {
			if (date == null) {
				continue;
			}
			availableDateSet.add(date.toString());
		}

		request.setAttribute("availableDateSet", availableDateSet);
		request.setAttribute("debugAvailableDateSet", availableDateSet);
		request.setAttribute("selectedDate", dateStr);
		request.setAttribute("selectedTime", timeStr);
		request.setAttribute("selectedCourseId", courseIdStr);

		List<?> timeList = new ArrayList<>();
		List<?> courseList = new ArrayList<>();
		// コース選択画面で各コースの残席数を赤文字表示するための course_id → 残席数 マップ
		Map<Long, Integer> capacityMap = new HashMap<>();

		if (!isBlank(dateStr)) {
			LocalDate selectedDate = LocalDate.parse(dateStr);
			timeList = dao.findStartTimesByDate(selectedDate);

			if (!isBlank(timeStr)) {
				LocalTime selectedTime = LocalTime.parse(timeStr);
				courseList = dao.findCoursesByDateAndTime(selectedDate, selectedTime);
				capacityMap = dao.findCapacityMapByDateAndTime(selectedDate, selectedTime);
			}
		}

		request.setAttribute("timeList", timeList);
		request.setAttribute("courseList", courseList);
		request.setAttribute("capacityMap", capacityMap);
	}

	private void forwardWithError(HttpServletRequest request, HttpServletResponse response,
			String errorMessage, String dateStr, String timeStr, String courseIdStr)
			throws Exception {

		request.setAttribute("error", errorMessage);
		loadReservationScreenData(request, dateStr, timeStr, courseIdStr);
		request.getRequestDispatcher("/jsp/user/reservation.jsp")
				.forward(request, response);
	}

	private String trim(String value) {
		return value == null ? null : value.trim();
	}

	private boolean isBlank(String value) {
		return value == null || value.isEmpty();
	}
}
