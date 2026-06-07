package com.pottery.reservation.dao;

import com.pottery.reservation.dto.CourseDTO;
import com.pottery.reservation.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * コーステーブルへのデータアクセスを担当するDAO。
 */
public class CourseDAO {

    /** 全コース取得(ID昇順) */
    public List<CourseDTO> findAll() {
        String sql = "SELECT * FROM courses ORDER BY course_id ASC";
        List<CourseDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** IDで1件取得 */
    public CourseDTO findById(int courseId) {
        String sql = "SELECT * FROM courses WHERE course_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** ResultSet → CourseDTO 変換 */
    private CourseDTO mapRow(ResultSet rs) throws SQLException {
        CourseDTO c = new CourseDTO();
        c.setCourseId(rs.getInt("course_id"));
        c.setName(rs.getString("name"));
        c.setDescription(rs.getString("description"));
        c.setPrice(rs.getInt("price"));
        c.setCreatedAt(rs.getTimestamp("created_at"));
        return c;
    }
}
