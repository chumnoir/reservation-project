package com.pottery.reservation.dao;

import com.pottery.reservation.dto.ReservationDTO;
import com.pottery.reservation.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 予約テーブルへのデータアクセスを担当するDAO。
 */
public class ReservationDAO {

    private static final String BASE_SELECT =
            "SELECT r.*, m.name AS member_name, m.email AS member_email, "
          + "       w.title AS workshop_title, w.event_date, w.start_time "
          + "FROM reservations r "
          + "JOIN members m   ON r.member_id   = m.member_id "
          + "JOIN workshops w ON r.workshop_id = w.workshop_id ";

    /** 予約登録 */
    public boolean insert(ReservationDTO r) {
        String sql = "INSERT INTO reservations (member_id, workshop_id, number_of_people, status) "
                   + "VALUES (?, ?, ?, 'CONFIRMED')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getMemberId());
            ps.setInt(2, r.getWorkshopId());
            ps.setInt(3, r.getNumberOfPeople());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 全予約取得(管理者用) */
    public List<ReservationDTO> findAll() {
        String sql = BASE_SELECT + "ORDER BY r.reserved_at DESC";
        List<ReservationDTO> list = new ArrayList<>();
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

    /** 会員ごとの予約取得(マイページ用) */
    public List<ReservationDTO> findByMemberId(int memberId) {
        String sql = BASE_SELECT + "WHERE r.member_id = ? ORDER BY w.event_date ASC";
        List<ReservationDTO> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    /** IDで1件取得 */
    public ReservationDTO findById(int reservationId) {
        String sql = BASE_SELECT + "WHERE r.reservation_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
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

    /** 同一会員が同一WSを既に予約済みか */
    public boolean existsReservation(int memberId, int workshopId) {
        String sql = "SELECT 1 FROM reservations WHERE member_id = ? AND workshop_id = ? AND status = 'CONFIRMED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            ps.setInt(2, workshopId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 予約人数・ステータスの更新(管理者用) */
    public boolean update(ReservationDTO r) {
        String sql = "UPDATE reservations SET number_of_people = ?, status = ? WHERE reservation_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, r.getNumberOfPeople());
            ps.setString(2, r.getStatus());
            ps.setInt(3, r.getReservationId());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 会員自身による予約キャンセル。
     * 「本人の」「確定状態(CONFIRMED)の」予約だけを CANCELED に更新する。
     * WHERE句に member_id と status を含めることで、他人の予約や
     * 既にキャンセル済みの予約を誤って操作できないようにしている。
     * @return 1件更新できれば true
     */
    public boolean cancelByMember(int reservationId, int memberId) {
        String sql = "UPDATE reservations SET status = 'CANCELED' "
                   + "WHERE reservation_id = ? AND member_id = ? AND status = 'CONFIRMED'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            ps.setInt(2, memberId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** 予約削除 */
    public boolean delete(int reservationId) {
        String sql = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** ResultSet → ReservationDTO 変換 */
    private ReservationDTO mapRow(ResultSet rs) throws SQLException {
        ReservationDTO r = new ReservationDTO();
        r.setReservationId(rs.getInt("reservation_id"));
        r.setMemberId(rs.getInt("member_id"));
        r.setWorkshopId(rs.getInt("workshop_id"));
        r.setNumberOfPeople(rs.getInt("number_of_people"));
        r.setStatus(rs.getString("status"));
        r.setReservedAt(rs.getTimestamp("reserved_at"));
        r.setMemberName(rs.getString("member_name"));
        r.setMemberEmail(rs.getString("member_email"));
        r.setWorkshopTitle(rs.getString("workshop_title"));
        r.setEventDate(rs.getDate("event_date"));
        r.setStartTime(rs.getTime("start_time"));
        return r;
    }
}
