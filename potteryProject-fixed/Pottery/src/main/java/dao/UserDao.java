package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.User;

public class UserDao {

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

	// 一覧取得
	public List<User> getAll() {
		List<User> list = new ArrayList<>();

		String sql = "SELECT * FROM tbl_user ORDER BY user_id";

		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement pstmt = con.prepareStatement(sql);
				ResultSet rs = pstmt.executeQuery()) {

			while (rs.next()) {
				User user = new User(
						rs.getLong("user_id"),
						rs.getString("email"),
						rs.getString("password"),
						rs.getString("user_name"),
						rs.getString("address"),
						rs.getString("phone_number"));
				list.add(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return list;
	}

	// 登録
	public boolean insert(User user) {

		String sql = "INSERT INTO tbl_user "
				+ "(user_name, email, password, phone_number, address) "
				+ "VALUES (?, ?, ?, ?, ?)";

		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, user.getUser_name());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getPhone_number());
			ps.setString(5, user.getAddress());

			int result = ps.executeUpdate();
			return result > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// メール重複チェック
	public boolean existsByEmail(String email) {

		String sql = "SELECT COUNT(*) FROM tbl_user WHERE email = ?";

		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, email);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return rs.getInt(1) > 0;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// ログイン用
	public User findByLogin(String email, String password) {

		String sql = "SELECT * FROM tbl_user WHERE email = ? AND password = ?";

		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, email);
			ps.setString(2, password);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getLong("user_id"),
							rs.getString("email"),
							rs.getString("password"),
							rs.getString("user_name"),
							rs.getString("address"),
							rs.getString("phone_number"));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public boolean update(User user) {

		String sql = "UPDATE tbl_user " +
				"SET user_name = ?, email = ?, password = ?, phone_number = ?, address = ? " +
				"WHERE user_id = ?";

		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {

			ps.setString(1, user.getUser_name());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getPassword());
			ps.setString(4, user.getPhone_number());
			ps.setString(5, user.getAddress());
			ps.setLong(6, user.getUser_id());

			int result = ps.executeUpdate();

			return result > 0;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// ===== 管理者用 =====

	public User findById(long id) {
		String sql = "SELECT * FROM tbl_user WHERE user_id = ?";
		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new User(
							rs.getLong("user_id"),
							rs.getString("email"),
							rs.getString("password"),
							rs.getString("user_name"),
							rs.getString("address"),
							rs.getString("phone_number"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 会員を削除する。tbl_book から参照されている（予約がある）と外部キー制約で
	 * 失敗するため、先にその会員の予約を削除してから会員本体を削除する。
	 */
	public boolean deleteById(long id) {
		Connection con = null;
		try {
			con = DriverManager.getConnection(URL, USER, PASS);
			con.setAutoCommit(false);

			try (PreparedStatement psBook =
					con.prepareStatement("DELETE FROM tbl_book WHERE user_id = ?")) {
				psBook.setLong(1, id);
				psBook.executeUpdate();
			}

			int userDeleted;
			try (PreparedStatement psUser =
					con.prepareStatement("DELETE FROM tbl_user WHERE user_id = ?")) {
				psUser.setLong(1, id);
				userDeleted = psUser.executeUpdate();
			}

			con.commit();
			return userDeleted == 1;

		} catch (Exception e) {
			e.printStackTrace();
			if (con != null) {
				try { con.rollback(); } catch (SQLException ignore) {}
			}
			return false;
		} finally {
			if (con != null) {
				try { con.close(); } catch (SQLException ignore) {}
			}
		}
	}
}