package com.pottery.reservation.dao;

import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 会員テーブルへのデータアクセスを担当するDAO。
 */
public class MemberDAO {

    /** 会員登録 */
    public boolean insert(MemberDTO member) {
        String sql = "INSERT INTO members (name, email, phone, address, password, role) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.setString(4, member.getAddress());
            ps.setString(5, member.getPassword());
            ps.setString(6, member.getRole() == null ? "USER" : member.getRole());
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** メールアドレスで会員を検索(ログイン用) */
    public MemberDTO findByEmail(String email) {
        String sql = "SELECT * FROM members WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
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

    /** IDで会員を検索 */
    public MemberDTO findById(int memberId) {
        String sql = "SELECT * FROM members WHERE member_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberId);
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

    /** メールアドレスの存在チェック(重複登録防止) */
    public boolean existsByEmail(String email) {
        return findByEmail(email) != null;
    }

    /** 全会員取得(管理者用、一般会員のみ表示する場合はrole条件を付与) */
    public List<MemberDTO> findAll() {
        String sql = "SELECT * FROM members ORDER BY created_at DESC";
        List<MemberDTO> list = new ArrayList<>();
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

    /** 会員削除(管理者用) */
    public boolean delete(int memberId) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, memberId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** ResultSet → MemberDTO 変換 */
    private MemberDTO mapRow(ResultSet rs) throws SQLException {
        MemberDTO m = new MemberDTO();
        m.setMemberId(rs.getInt("member_id"));
        m.setName(rs.getString("name"));
        m.setEmail(rs.getString("email"));
        m.setPhone(rs.getString("phone"));
        m.setAddress(rs.getString("address"));
        m.setPassword(rs.getString("password"));
        m.setRole(rs.getString("role"));
        m.setCreatedAt(rs.getTimestamp("created_at"));
        return m;
    }
}
