package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dto.Info;

public class InfoDao {

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

    // ✅ 全件取得（メイン用）
    public List<Info> findAll() {
        List<Info> list = new ArrayList<>();

        String sql = "SELECT * FROM tbl_info ORDER BY post_date DESC";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Info info = new Info();
                info.setInfo_id(rs.getLong("info_id"));
                info.setTitle(rs.getString("title"));
                info.setContent(rs.getString("content"));

                if (rs.getDate("post_date") != null) {
                    info.setPost_date(rs.getDate("post_date").toLocalDate());
                }

                list.add(info);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        // ✅ デバッグ
        System.out.println("取得件数：" + list.size());

        return list;
    }

    // ✅ 1件取得（詳細ページ用）
    public Info findById(long id) {
        Info info = null;

        String sql = "SELECT * FROM tbl_info WHERE info_id = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                info = new Info();
                info.setInfo_id(rs.getLong("info_id"));
                info.setTitle(rs.getString("title"));
                info.setContent(rs.getString("content"));

                if (rs.getDate("post_date") != null) {
                    info.setPost_date(rs.getDate("post_date").toLocalDate());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    // ✅ 追加（管理者用）
    public boolean insert(Info info) {
        String sql = "INSERT INTO tbl_info (title, content, post_date) VALUES (?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, info.getTitle());
            ps.setString(2, info.getContent());
            if (info.getPost_date() != null) {
                ps.setDate(3, java.sql.Date.valueOf(info.getPost_date()));
            } else {
                ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            }
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 更新（管理者用）
    public boolean update(Info info) {
        String sql = "UPDATE tbl_info SET title = ?, content = ?, post_date = ? WHERE info_id = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, info.getTitle());
            ps.setString(2, info.getContent());
            if (info.getPost_date() != null) {
                ps.setDate(3, java.sql.Date.valueOf(info.getPost_date()));
            } else {
                ps.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
            }
            ps.setLong(4, info.getInfo_id());
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ 削除（管理者用）
    public boolean deleteById(long id) {
        String sql = "DELETE FROM tbl_info WHERE info_id = ?";

        try (Connection con = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() == 1;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}