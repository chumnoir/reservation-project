package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Course;

//import foo.bar.dto.Course;

public class CourseDao{
 private final String URL = "jdbc:postgresql://localhost:5432/pottery";
 private final String USER = "postgres";
 private final String PASS = "password";

 static{
  try{
   Class.forName("org.postgresql.Driver");
  } catch(ClassNotFoundException e){
     e.printStackTrace();
  }
}
 
 	static {
 		try {
 			Class.forName("org.postgresql.Driver");
 		} catch (ClassNotFoundException e) {
 			e.printStackTrace();
 		}

 	}

 	public List<Course> getAll() {
 		List<Course> list = new ArrayList<>();

 		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
 				PreparedStatement pstmt = con.prepareStatement(
 						"SELECT * FROM tbl_course ORDER BY course_id")) {

 			System.out.printf("[sql] %s%n", pstmt); // debug
 			ResultSet rs = pstmt.executeQuery();

 			while (rs.next()) {
 				Course course = new Course(
 						rs.getLong("course_id"),
 						rs.getString("course_name"),
 						rs.getInt("price"),
 						rs.getInt("required_time"),
 						rs.getString("description")

 				);
 				list.add(course);
 			}

 		} catch (SQLException e) {
 			e.printStackTrace();
 		}

 		return list;
 	}

	// ===== 管理者用 =====

	public Course findById(long id) {
		String sql = "SELECT * FROM tbl_course WHERE course_id = ?";
		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new Course(
							rs.getLong("course_id"),
							rs.getString("course_name"),
							rs.getInt("price"),
							rs.getInt("required_time"),
							rs.getString("description"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean insert(Course course) {
		String sql = "INSERT INTO tbl_course (course_name, price, required_time, description) "
				+ "VALUES (?, ?, ?, ?)";
		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, course.getCourse_name());
			ps.setInt(2, course.getPrice());
			ps.setInt(3, course.getRequired_time());
			ps.setString(4, course.getDescription());
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean update(Course course) {
		String sql = "UPDATE tbl_course SET course_name = ?, price = ?, required_time = ?, "
				+ "description = ? WHERE course_id = ?";
		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setString(1, course.getCourse_name());
			ps.setInt(2, course.getPrice());
			ps.setInt(3, course.getRequired_time());
			ps.setString(4, course.getDescription());
			ps.setLong(5, course.getCourse_id());
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * コース削除。tbl_workshop から参照されている場合は外部キー制約で失敗し false を返す。
	 */
	public boolean deleteById(long id) {
		String sql = "DELETE FROM tbl_course WHERE course_id = ?";
		try (Connection con = DriverManager.getConnection(URL, USER, PASS);
				PreparedStatement ps = con.prepareStatement(sql)) {
			ps.setLong(1, id);
			return ps.executeUpdate() == 1;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

 }
