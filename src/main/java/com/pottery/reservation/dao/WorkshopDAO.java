package com.pottery.reservation.dao;

import com.pottery.reservation.dto.WorkshopDTO;
import com.pottery.reservation.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * ワークショップテーブルへのデータアクセスを担当するDAO。
 * 予約人数(reserved_count)はCONFIRMED予約を集計して取得する。
 */
public class WorkshopDAO {

    private static final String BASE_SELECT =
            "SELECT w.*, "
          + "  COALESCE(SUM(CASE WHEN r.status = 'CONFIRMED' THEN r.number_of_people ELSE 0 END), 0) AS reserved_count "
          + "FROM workshops w "
          + "LEFT JOIN reservations r ON w.workshop_id = r.workshop_id ";

    /** 全ワークショップ取得(開催日昇順) */
    public List<WorkshopDTO> findAll() {
        String sql = BASE_SELECT + "GROUP BY w.workshop_id ORDER BY w.event_date ASC, w.start_time ASC";
        List<WorkshopDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** IDで1件取得 */
    public WorkshopDTO findById(int workshopId) {
        String sql = BASE_SELECT + "WHERE w.workshop_id = ? GROUP BY w.workshop_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workshopId);
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
    public boolean insert(WorkshopDTO w) {
        String sql = "INSERT INTO workshops (title, description, instructor, event_date, start_time, capacity, price) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, w.getTitle());
            ps.setString(2, w.getDescription());
            ps.setString(3, w.getInstructor());
            ps.setDate(4, w.getEventDate());
            ps.setTime(5, w.getStartTime());
            ps.setInt(6, w.getCapacity());
            ps.setInt(7, w.getPrice());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 更新 */
    public boolean update(WorkshopDTO w) {
        String sql = "UPDATE workshops SET title=?, description=?, instructor=?, event_date=?, "
                   + "start_time=?, capacity=?, price=? WHERE workshop_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, w.getTitle());
            ps.setString(2, w.getDescription());
            ps.setString(3, w.getInstructor());
            ps.setDate(4, w.getEventDate());
            ps.setTime(5, w.getStartTime());
            ps.setInt(6, w.getCapacity());
            ps.setInt(7, w.getPrice());
            ps.setInt(8, w.getWorkshopId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 削除 */
    public boolean delete(int workshopId) {
        String sql = "DELETE FROM workshops WHERE workshop_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, workshopId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** ResultSet → WorkshopDTO 変換 */
    private WorkshopDTO mapRow(ResultSet rs) throws SQLException {
        WorkshopDTO w = new WorkshopDTO();
        w.setWorkshopId(rs.getInt("workshop_id"));
        w.setTitle(rs.getString("title"));
        w.setDescription(rs.getString("description"));
        w.setInstructor(rs.getString("instructor"));
        w.setEventDate(rs.getDate("event_date"));
        w.setStartTime(rs.getTime("start_time"));
        w.setCapacity(rs.getInt("capacity"));
        w.setPrice(rs.getInt("price"));
        w.setCreatedAt(rs.getTimestamp("created_at"));
        w.setReservedCount(rs.getInt("reserved_count"));
        return w;
    }
}
