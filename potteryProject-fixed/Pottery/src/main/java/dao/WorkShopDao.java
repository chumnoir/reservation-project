package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dto.Course;
import dto.WorkshopDisplay;

public class WorkShopDao {

	/**
	 * カレンダー表示用：予約可能日一覧を取得
	 *
	 * 使用箇所:
	 * ReservationServlet
	 * calendar.jsp
	 *
	 * 取得元:
	 * tbl_workshop.workshop_date
	 */
	private static final String URL = "jdbc:postgresql://localhost:5432/pottery";
	private static final String USER = "postgres";
	private static final String PASS = "password";

	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Set<LocalDate> findAvailableDates() {

		Set<LocalDate> dateSet = new LinkedHashSet<>();

		String sql = """
				SELECT DISTINCT
				    workshop_date
				FROM tbl_workshop
				ORDER BY workshop_date
				""";

		System.out.println("===== WorkShopDao.findAvailableDates start =====");

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				Date sqlDate = rs.getDate("workshop_date");

				if (sqlDate != null) {
					dateSet.add(sqlDate.toLocalDate());
				}
			}

			System.out.println("available date size = " + dateSet.size());

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findAvailableDates error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return dateSet;
	}

	/**
	 * 選択された日付に対応する開始時刻一覧を取得
	 *
	 * 使用箇所:
	 * reservation.jsp の時間select
	 *
	 * @param date 選択された開催日
	 * @return 開始時刻一覧
	 */
	public List<LocalTime> findStartTimesByDate(LocalDate date) {

		List<LocalTime> timeList = new ArrayList<>();

		String sql = """
				SELECT DISTINCT
				    start_time
				FROM tbl_workshop
				WHERE workshop_date = ?
				ORDER BY start_time
				""";

		System.out.println("===== WorkShopDao.findStartTimesByDate start =====");
		System.out.println("date = " + date);

		if (date == null) {
			System.out.println("date is null");
			return timeList;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setDate(1, Date.valueOf(date));

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Time sqlTime = rs.getTime("start_time");

					if (sqlTime != null) {
						timeList.add(sqlTime.toLocalTime());
					}
				}
			}

			System.out.println("time list size = " + timeList.size());

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findStartTimesByDate error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return timeList;
	}

	/**
	 * 選択された日付・開始時刻に対応するコース一覧を取得
	 *
	 * 使用箇所:
	 * reservation.jsp のコース選択
	 *
	 * 取得元:
	 * tbl_workshop
	 * tbl_course
	 *
	 * @param date 開催日
	 * @param time 開始時刻
	 * @return コース一覧
	 */
	public List<Course> findCoursesByDateAndTime(LocalDate date, LocalTime time) {

		List<Course> courseList = new ArrayList<>();

		String sql = """
				SELECT
				    c.course_id,
				    c.course_name,
				    c.price,
				    c.required_time,
				    c.description
				FROM tbl_workshop w
				JOIN tbl_course c
				    ON w.course_id = c.course_id
				WHERE w.workshop_date = ?
				  AND w.start_time = ?
				ORDER BY c.course_id
				""";

		System.out.println("===== WorkShopDao.findCoursesByDateAndTime start =====");
		System.out.println("date = " + date);
		System.out.println("time = " + time);

		if (date == null || time == null) {
			System.out.println("date or time is null");
			return courseList;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setDate(1, Date.valueOf(date));
			ps.setTime(2, Time.valueOf(time));

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Course course = new Course();

					course.setCourse_id(rs.getLong("course_id"));
					course.setCourse_name(rs.getString("course_name"));
					course.setPrice(rs.getInt("price"));
					course.setRequired_time(rs.getInt("required_time"));
					course.setDescription(rs.getString("description"));

					courseList.add(course);
				}
			}

			System.out.println("course list size = " + courseList.size());

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findCoursesByDateAndTime error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return courseList;
	}

	/**
	 * 日付・開始時刻・コースIDから workshop_id を取得
	 *
	 * 使用箇所:
	 * ReservationServlet
	 * ReservationCompleteServlet
	 *
	 * @param date 開催日
	 * @param time 開始時刻
	 * @param courseId コースID
	 * @return workshop_id。見つからない場合は null
	 */
	public Integer findWorkshopId(LocalDate date, LocalTime time, Long courseId) {

		String sql = """
				SELECT
				    workshop_id
				FROM tbl_workshop
				WHERE workshop_date = ?
				  AND start_time = ?
				  AND course_id = ?
				""";

		System.out.println("===== WorkShopDao.findWorkshopId start =====");
		System.out.println("date = " + date);
		System.out.println("time = " + time);
		System.out.println("courseId = " + courseId);

		if (date == null || time == null || courseId == null) {
			System.out.println("date, time, or courseId is null");
			return null;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setDate(1, Date.valueOf(date));
			ps.setTime(2, Time.valueOf(time));
			ps.setLong(3, courseId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int workshopId = rs.getInt("workshop_id");

					System.out.println("found workshopId = " + workshopId);

					return workshopId;
				}
			}

			System.out.println("workshopId not found");

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findWorkshopId error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ワークショップ一覧画面用の表示データを取得
	 *
	 * 使用箇所:
	 * WorkshopListServlet
	 * workshopList.jsp
	 *
	 * 表示項目:
	 * - コース名
	 * - 開催日
	 * - 費用
	 * - 開始時刻
	 * - 説明
	 *
	 * 取得元:
	 * tbl_workshop
	 * tbl_course
	 *
	 * @return ワークショップ表示用リスト
	 */
	public List<WorkshopDisplay> findWorkshopDisplays() {

		List<WorkshopDisplay> workshopList = new ArrayList<>();

		String sql = """
				SELECT
				    w.workshop_id,
				    w.course_id,
				    w.workshop_date,
				    w.start_time,
				    w.capacity,
				    c.course_name,
				    c.price,
				    c.required_time,
				    c.description
				FROM tbl_workshop w
				JOIN tbl_course c
				    ON w.course_id = c.course_id
				ORDER BY
				    w.workshop_date ASC,
				    w.start_time ASC,
				    w.workshop_id ASC
				""";

		System.out.println("===== WorkShopDao.findWorkshopDisplays start =====");

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			while (rs.next()) {
				WorkshopDisplay workshop = new WorkshopDisplay();

				workshop.setWorkshop_id(rs.getLong("workshop_id"));
				workshop.setCourse_id(rs.getLong("course_id"));

				Date sqlDate = rs.getDate("workshop_date");
				if (sqlDate != null) {
					workshop.setWorkshop_date(sqlDate.toLocalDate());
				}

				Time sqlTime = rs.getTime("start_time");
				if (sqlTime != null) {
					workshop.setStart_time(sqlTime.toLocalTime());
				}

				workshop.setCapacity(rs.getInt("capacity"));
				workshop.setCourse_name(rs.getString("course_name"));
				workshop.setPrice(rs.getInt("price"));
				workshop.setRequired_time(rs.getInt("required_time"));
				workshop.setDescription(rs.getString("description"));

				workshopList.add(workshop);
			}

			System.out.println("workshop display size = " + workshopList.size());

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findWorkshopDisplays error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return workshopList;
	}

	/**
	 * course_id からコース情報を1件取得
	 *
	 * 必須ではないが、今後詳細画面を作る場合に使える。
	 *
	 * @param courseId コースID
	 * @return Course。見つからない場合は null
	 */
	public Course findCourseById(Long courseId) {

		String sql = """
				SELECT
				    course_id,
				    course_name,
				    price,
				    required_time,
				    description
				FROM tbl_course
				WHERE course_id = ?
				""";

		System.out.println("===== WorkShopDao.findCourseById start =====");
		System.out.println("courseId = " + courseId);

		if (courseId == null) {
			System.out.println("courseId is null");
			return null;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, courseId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					Course course = new Course();

					course.setCourse_id(rs.getLong("course_id"));
					course.setCourse_name(rs.getString("course_name"));
					course.setPrice(rs.getInt("price"));
					course.setRequired_time(rs.getInt("required_time"));
					course.setDescription(rs.getString("description"));

					return course;
				}
			}

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findCourseById error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 指定した日付・開始時刻について、コースごとの残席数を取得する。
	 *
	 * 使用箇所:
	 * ReservationServlet（コース選択画面で各コースの残席数を表示するため）
	 *
	 * 取得元:
	 * tbl_workshop（workshop_date・start_time が一致する行の course_id と capacity）
	 *
	 * @param date 開催日
	 * @param time 開始時刻
	 * @return course_id をキー、残席数を値とするマップ（該当なしの場合は空マップ）
	 */
	public Map<Long, Integer> findCapacityMapByDateAndTime(LocalDate date, LocalTime time) {

		Map<Long, Integer> capacityMap = new LinkedHashMap<>();

		String sql = """
				SELECT
				    course_id,
				    capacity
				FROM tbl_workshop
				WHERE workshop_date = ?
				  AND start_time = ?
				""";

		System.out.println("===== WorkShopDao.findCapacityMapByDateAndTime start =====");
		System.out.println("date = " + date);
		System.out.println("time = " + time);

		if (date == null || time == null) {
			System.out.println("date or time is null");
			return capacityMap;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setDate(1, Date.valueOf(date));
			ps.setTime(2, Time.valueOf(time));

			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					capacityMap.put(rs.getLong("course_id"), rs.getInt("capacity"));
				}
			}

			System.out.println("capacity map size = " + capacityMap.size());

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findCapacityMapByDateAndTime error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return capacityMap;
	}

	/**
	 * workshop_id から現在の残席数（capacity）を取得する。
	 *
	 * 使用箇所:
	 * ReservationServlet（予約確認画面に残席数を表示するため）
	 *
	 * 取得元:
	 * tbl_workshop.capacity
	 *
	 * @param workshopId 対象ワークショップID
	 * @return 残席数。見つからない場合は null
	 */
	public Integer findCapacityById(int workshopId) {

		String sql = """
				SELECT
				    capacity
				FROM tbl_workshop
				WHERE workshop_id = ?
				""";

		System.out.println("===== WorkShopDao.findCapacityById start =====");
		System.out.println("workshopId = " + workshopId);

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, workshopId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					int capacity = rs.getInt("capacity");
					System.out.println("capacity = " + capacity);
					return capacity;
				}
			}

			System.out.println("workshop not found");

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findCapacityById error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 予約確定時に、対象ワークショップの定員（残り枠数）を減らす。
	 *
	 * 使用箇所:
	 * ReservationCompleteServlet
	 *
	 * 更新先:
	 * tbl_workshop.capacity
	 *
	 * 「capacity >= ?」を WHERE 条件に入れることで、
	 * 残り枠が足りないときは1件も更新されず（戻り値 false）、
	 * 同時アクセス時の定員オーバー（マイナス在庫）を防ぐ。
	 *
	 * @param workshopId 対象ワークショップID
	 * @param num        予約人数（減らす枠数）
	 * @return 定員を減らせたら true。残り枠不足などで更新できなければ false
	 */
	public boolean decreaseCapacity(int workshopId, int num) {

		String sql = """
				UPDATE tbl_workshop
				SET capacity = capacity - ?
				WHERE workshop_id = ?
				  AND capacity >= ?
				""";

		System.out.println("===== WorkShopDao.decreaseCapacity start =====");
		System.out.println("workshopId = " + workshopId);
		System.out.println("num = " + num);

		if (num <= 0) {
			System.out.println("num <= 0");
			return false;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, num);
			ps.setInt(2, workshopId);
			ps.setInt(3, num);

			int count = ps.executeUpdate();

			System.out.println("decreaseCapacity update count = " + count);

			return count == 1;

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.decreaseCapacity error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 定員を元に戻す（増やす）。
	 *
	 * 使用箇所:
	 * ReservationCompleteServlet
	 *   （定員を減らした後に予約登録が失敗したときの巻き戻し用）
	 *
	 * @param workshopId 対象ワークショップID
	 * @param num        戻す枠数
	 * @return 更新できたら true
	 */
	public boolean increaseCapacity(int workshopId, int num) {

		String sql = """
				UPDATE tbl_workshop
				SET capacity = capacity + ?
				WHERE workshop_id = ?
				""";

		System.out.println("===== WorkShopDao.increaseCapacity start =====");
		System.out.println("workshopId = " + workshopId);
		System.out.println("num = " + num);

		if (num <= 0) {
			System.out.println("num <= 0");
			return false;
		}

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, num);
			ps.setInt(2, workshopId);

			int count = ps.executeUpdate();

			System.out.println("increaseCapacity update count = " + count);

			return count == 1;

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.increaseCapacity error =====");
			System.out.println("message = " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	/* ============================================================
	 * 管理者用：ワークショップ開催枠の CRUD
	 * ============================================================ */

	/** workshop_id から表示用のワークショップ1件を取得 */
	public WorkshopDisplay findWorkshopDisplayById(long workshopId) {

		String sql = """
				SELECT
				    w.workshop_id,
				    w.course_id,
				    w.workshop_date,
				    w.start_time,
				    w.capacity,
				    c.course_name,
				    c.price,
				    c.required_time,
				    c.description
				FROM tbl_workshop w
				JOIN tbl_course c ON w.course_id = c.course_id
				WHERE w.workshop_id = ?
				""";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, workshopId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					WorkshopDisplay w = new WorkshopDisplay();
					w.setWorkshop_id(rs.getLong("workshop_id"));
					w.setCourse_id(rs.getLong("course_id"));
					Date d = rs.getDate("workshop_date");
					if (d != null) {
						w.setWorkshop_date(d.toLocalDate());
					}
					Time t = rs.getTime("start_time");
					if (t != null) {
						w.setStart_time(t.toLocalTime());
					}
					w.setCapacity(rs.getInt("capacity"));
					w.setCourse_name(rs.getString("course_name"));
					w.setPrice(rs.getInt("price"));
					w.setRequired_time(rs.getInt("required_time"));
					w.setDescription(rs.getString("description"));
					return w;
				}
			}

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.findWorkshopDisplayById error =====");
			e.printStackTrace();
		}

		return null;
	}

	/** ワークショップ開催枠を追加 */
	public boolean insertWorkshop(long courseId, LocalDate date, LocalTime time, int capacity) {

		String sql = """
				INSERT INTO tbl_workshop (course_id, workshop_date, start_time, capacity)
				VALUES (?, ?, ?, ?)
				""";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, courseId);
			ps.setDate(2, Date.valueOf(date));
			ps.setTime(3, Time.valueOf(time));
			ps.setInt(4, capacity);
			return ps.executeUpdate() == 1;

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.insertWorkshop error =====");
			e.printStackTrace();
			return false;
		}
	}

	/** ワークショップ開催枠を更新 */
	public boolean updateWorkshop(long workshopId, long courseId,
			LocalDate date, LocalTime time, int capacity) {

		String sql = """
				UPDATE tbl_workshop
				SET course_id = ?, workshop_date = ?, start_time = ?, capacity = ?
				WHERE workshop_id = ?
				""";

		try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, courseId);
			ps.setDate(2, Date.valueOf(date));
			ps.setTime(3, Time.valueOf(time));
			ps.setInt(4, capacity);
			ps.setLong(5, workshopId);
			return ps.executeUpdate() == 1;

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.updateWorkshop error =====");
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * ワークショップ開催枠を削除。
	 * その枠への予約（tbl_book）があると外部キー制約で削除できないため、
	 * 先に予約を削除してから枠本体を削除する（トランザクション）。
	 */
	public boolean deleteWorkshop(long workshopId) {

		Connection conn = null;
		try {
			conn = DriverManager.getConnection(URL, USER, PASS);
			conn.setAutoCommit(false);

			try (PreparedStatement psBook =
					conn.prepareStatement("DELETE FROM tbl_book WHERE workshop_id = ?")) {
				psBook.setLong(1, workshopId);
				psBook.executeUpdate();
			}

			int deleted;
			try (PreparedStatement psWs =
					conn.prepareStatement("DELETE FROM tbl_workshop WHERE workshop_id = ?")) {
				psWs.setLong(1, workshopId);
				deleted = psWs.executeUpdate();
			}

			conn.commit();
			return deleted == 1;

		} catch (Exception e) {
			System.out.println("===== WorkShopDao.deleteWorkshop error =====");
			e.printStackTrace();
			if (conn != null) {
				try { conn.rollback(); } catch (Exception ignore) {}
			}
			return false;
		} finally {
			if (conn != null) {
				try { conn.close(); } catch (Exception ignore) {}
			}
		}
	}
}