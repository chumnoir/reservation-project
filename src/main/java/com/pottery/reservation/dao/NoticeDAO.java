package com.pottery.reservation.dao;

import com.pottery.reservation.dto.NoticeDTO;
import com.pottery.reservation.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * お知らせテーブルへのデータアクセスを担当するDAO。
 */
public class NoticeDAO {

    /** 全お知らせ取得(新しい順) */
    public List<NoticeDTO> findAll() {
        String sql = "SELECT * FROM notices ORDER BY created_at DESC";
        List<NoticeDTO> list = new ArrayList<>();
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
    public NoticeDTO findById(int noticeId) {
        String sql = "SELECT * FROM notices WHERE notice_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, noticeId);
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

    /** 追加 */
    public boolean insert(NoticeDTO n) {
        String sql = "INSERT INTO notices (title, content) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, n.getTitle());
            ps.setString(2, n.getContent());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 更新 */
    public boolean update(NoticeDTO n) {
        String sql = "UPDATE notices SET title = ?, content = ? WHERE notice_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, n.getTitle());
            ps.setString(2, n.getContent());
            ps.setInt(3, n.getNoticeId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 削除 */
    public boolean delete(int noticeId) {
        String sql = "DELETE FROM notices WHERE notice_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, noticeId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** ResultSet → NoticeDTO 変換 */
    private NoticeDTO mapRow(ResultSet rs) throws SQLException {
        NoticeDTO n = new NoticeDTO();
        n.setNoticeId(rs.getInt("notice_id"));
        n.setTitle(rs.getString("title"));
        n.setContent(rs.getString("content"));
        n.setCreatedAt(rs.getTimestamp("created_at"));
        n.setUpdatedAt(rs.getTimestamp("updated_at"));
        return n;
    }
}
