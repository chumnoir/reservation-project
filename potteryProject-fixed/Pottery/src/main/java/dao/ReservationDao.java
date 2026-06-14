package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.Reservation;
import dto.ReservationHistory;

public class ReservationDao {
	private final String URL = "jdbc:postgresql://localhost:5432/pottery";
    private final String USER = "postgres";
    private final String PASS = "password";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 予約登録
     *
     * 実テーブル:
     * tbl_book(book_id, workshop_id, user_id, guest_name, num_people)
     */
    public boolean insert(Reservation reservation) {

        String sql = """
                INSERT INTO tbl_book (
                    workshop_id,
                    user_id,
                    guest_name,
                    num_people
                )
                VALUES (?, ?, ?, ?)
                """;

        System.out.println("===== ReservationDao.insert start =====");
        System.out.println("SQL = " + sql);
        System.out.println("workshop_id = " + reservation.getWorkshop_id());
        System.out.println("user_id = " + reservation.getUser_id());
        System.out.println("guest_name = " + reservation.getGuest_id());
        System.out.println("num_people = " + reservation.getNum_people());

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getWorkshop_id());
            ps.setInt(2, reservation.getUser_id());
            ps.setString(3, reservation.getGuest_id());
            ps.setInt(4, reservation.getNum_people());

            int count = ps.executeUpdate();

            System.out.println("insert count = " + count);

            return count == 1;

        } catch (Exception e) {
            System.out.println("===== ReservationDao.insert error =====");
            System.out.println("message = " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ログインユーザーの予約履歴取得
     */
    public List<ReservationHistory> getHistoryByUserId(Long userId) {

        List<ReservationHistory> list = new ArrayList<>();

        String sql = """
                SELECT
                    b.book_id,
                    c.course_name,
                    w.workshop_date,
                    w.start_time,
                    b.guest_name,
                    b.num_people,
                    c.price * b.num_people AS total_price
                FROM tbl_book b
                JOIN tbl_workshop w
                    ON b.workshop_id = w.workshop_id
                JOIN tbl_course c
                    ON w.course_id = c.course_id
                WHERE b.user_id = ?
                ORDER BY w.workshop_date DESC, w.start_time DESC
                """;

        System.out.println("===== ReservationDao.getHistoryByUserId start =====");
        System.out.println("userId = " + userId);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ReservationHistory history = new ReservationHistory();

                    history.setBook_id(rs.getLong("book_id"));
                    history.setCourse_name(rs.getString("course_name"));
                    history.setWorkshop_date(String.valueOf(rs.getDate("workshop_date")));
                    history.setStart_time(String.valueOf(rs.getTime("start_time")));
                    history.setGuest_name(rs.getString("guest_name"));
                    history.setNum_people(rs.getInt("num_people"));
                    history.setTotal_price(rs.getInt("total_price"));

                    list.add(history);
                }
            }

            System.out.println("history size = " + list.size());

        } catch (Exception e) {
            System.out.println("===== ReservationDao.getHistoryByUserId error =====");
            System.out.println("message = " + e.getMessage());
            e.printStackTrace();
        }

        return list;
    }

    /**
     * キャンセル確認用：予約1件取得
     */
    public ReservationHistory getHistoryByBookId(Long bookId, Long userId) {

        String sql = """
                SELECT
                    b.book_id,
                    c.course_name,
                    w.workshop_date,
                    w.start_time,
                    b.guest_name,
                    b.num_people,
                    c.price * b.num_people AS total_price
                FROM tbl_book b
                JOIN tbl_workshop w
                    ON b.workshop_id = w.workshop_id
                JOIN tbl_course c
                    ON w.course_id = c.course_id
                WHERE b.book_id = ?
                  AND b.user_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookId);
            ps.setLong(2, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationHistory history = new ReservationHistory();

                    history.setBook_id(rs.getLong("book_id"));
                    history.setCourse_name(rs.getString("course_name"));
                    history.setWorkshop_date(String.valueOf(rs.getDate("workshop_date")));
                    history.setStart_time(String.valueOf(rs.getTime("start_time")));
                    history.setGuest_name(rs.getString("guest_name"));
                    history.setNum_people(rs.getInt("num_people"));
                    history.setTotal_price(rs.getInt("total_price"));

                    return history;
                }
            }

        } catch (Exception e) {
            System.out.println("===== ReservationDao.getHistoryByBookId error =====");
            System.out.println("message = " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 予約キャンセル
     * 物理削除版
     */
    public boolean cancelReservation(Long bookId, Long userId) {

        String sql = """
                DELETE FROM tbl_book
                WHERE book_id = ?
                  AND user_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookId);
            ps.setLong(2, userId);

            int count = ps.executeUpdate();

            return count == 1;

        } catch (Exception e) {
            System.out.println("===== ReservationDao.cancelReservation error =====");
            System.out.println("message = " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /* ============================================================
     * 管理者用メソッド
     * ============================================================ */

    /** 管理者用：全予約を表示用に取得 */
    public List<ReservationHistory> getAllHistory() {

        List<ReservationHistory> list = new ArrayList<>();

        String sql = """
                SELECT
                    b.book_id,
                    c.course_name,
                    w.workshop_date,
                    w.start_time,
                    b.guest_name,
                    b.num_people,
                    c.price * b.num_people AS total_price
                FROM tbl_book b
                JOIN tbl_workshop w ON b.workshop_id = w.workshop_id
                JOIN tbl_course c ON w.course_id = c.course_id
                ORDER BY w.workshop_date DESC, w.start_time DESC, b.book_id DESC
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ReservationHistory h = new ReservationHistory();
                h.setBook_id(rs.getLong("book_id"));
                h.setCourse_name(rs.getString("course_name"));
                h.setWorkshop_date(String.valueOf(rs.getDate("workshop_date")));
                h.setStart_time(String.valueOf(rs.getTime("start_time")));
                h.setGuest_name(rs.getString("guest_name"));
                h.setNum_people(rs.getInt("num_people"));
                h.setTotal_price(rs.getInt("total_price"));
                list.add(h);
            }

        } catch (Exception e) {
            System.out.println("===== ReservationDao.getAllHistory error =====");
            e.printStackTrace();
        }

        return list;
    }

    /** 管理者用：book_id から表示用の予約1件を取得 */
    public ReservationHistory findHistoryByBookId(long bookId) {

        String sql = """
                SELECT
                    b.book_id,
                    c.course_name,
                    w.workshop_date,
                    w.start_time,
                    b.guest_name,
                    b.num_people,
                    c.price * b.num_people AS total_price
                FROM tbl_book b
                JOIN tbl_workshop w ON b.workshop_id = w.workshop_id
                JOIN tbl_course c ON w.course_id = c.course_id
                WHERE b.book_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ReservationHistory h = new ReservationHistory();
                    h.setBook_id(rs.getLong("book_id"));
                    h.setCourse_name(rs.getString("course_name"));
                    h.setWorkshop_date(String.valueOf(rs.getDate("workshop_date")));
                    h.setStart_time(String.valueOf(rs.getTime("start_time")));
                    h.setGuest_name(rs.getString("guest_name"));
                    h.setNum_people(rs.getInt("num_people"));
                    h.setTotal_price(rs.getInt("total_price"));
                    return h;
                }
            }

        } catch (Exception e) {
            System.out.println("===== ReservationDao.findHistoryByBookId error =====");
            e.printStackTrace();
        }

        return null;
    }

    /** 管理者用：book_id から予約の生データ（workshop_id・人数など）を取得 */
    public Reservation findByBookId(long bookId) {

        String sql = """
                SELECT book_id, workshop_id, user_id, guest_name, num_people
                FROM tbl_book
                WHERE book_id = ?
                """;

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Reservation(
                            rs.getLong("book_id"),
                            rs.getInt("workshop_id"),
                            rs.getInt("user_id"),
                            rs.getString("guest_name"),
                            rs.getInt("num_people"),
                            0);
                }
            }

        } catch (Exception e) {
            System.out.println("===== ReservationDao.findByBookId error =====");
            e.printStackTrace();
        }

        return null;
    }

    /** 管理者用：予約の代表者名・人数を更新 */
    public boolean updateGuestAndNum(long bookId, String guestName, int numPeople) {

        String sql = "UPDATE tbl_book SET guest_name = ?, num_people = ? WHERE book_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, guestName);
            ps.setInt(2, numPeople);
            ps.setLong(3, bookId);
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            System.out.println("===== ReservationDao.updateGuestAndNum error =====");
            e.printStackTrace();
            return false;
        }
    }

    /** 管理者用：book_id で予約を削除 */
    public boolean deleteByBookId(long bookId) {

        String sql = "DELETE FROM tbl_book WHERE book_id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, bookId);
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            System.out.println("===== ReservationDao.deleteByBookId error =====");
            e.printStackTrace();
            return false;
        }
    }
}