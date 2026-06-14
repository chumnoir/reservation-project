package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Admin;

public class AdminDao {

    private final String URL = "jdbc:postgresql://localhost:5432/pottery";
    private final String USER = "postgres";
    private final String PASS = "password";

    // ドライバロード
    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // ✅ 全件取得
    public List<Admin> getAll() {
        List<Admin> list = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT * FROM tbl_admin ORDER BY admin_id")) {

            System.out.printf("[sql] %s%n", pstmt);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Admin admin = new Admin(
                        rs.getLong("admin_id"),
                        rs.getString("admin_name"),
                        rs.getString("admin_email"),
                        rs.getString("admin_password")
                );
                list.add(admin);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    // ✅ ログイン用（追加したメソッド）
    public Admin findByEmailAndPassword(String email, String password) {

        Admin admin = null;

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement pstmt = con.prepareStatement(
                     "SELECT * FROM tbl_admin WHERE admin_email = ? AND admin_password = ?")) {

            pstmt.setString(1, email);
            pstmt.setString(2, password);

            System.out.printf("[sql] %s%n", pstmt);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                admin = new Admin(
                        rs.getLong("admin_id"),
                        rs.getString("admin_name"),
                        rs.getString("admin_email"),
                        rs.getString("admin_password")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return admin;
    }
}