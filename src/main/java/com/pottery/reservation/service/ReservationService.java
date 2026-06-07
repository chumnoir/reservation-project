package com.pottery.reservation.service;

import com.pottery.reservation.dao.CourseDAO;
import com.pottery.reservation.dao.ReservationDAO;
import com.pottery.reservation.dao.WorkshopDAO;
import com.pottery.reservation.dto.ReservationDTO;
import com.pottery.reservation.dto.WorkshopDTO;

import java.util.List;

/**
 * 予約に関する業務ロジックを担当するService。
 * 重複予約・定員超過のチェックを行う。
 */
public class ReservationService {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final WorkshopDAO workshopDAO = new WorkshopDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    /**
     * 予約を作成する。選択したコース(courseId)も保存する。
     * @throws IllegalArgumentException 入力不備・コース未選択時
     * @throws IllegalStateException 重複予約・満席時
     */
    public boolean reserve(int memberId, int workshopId, int courseId, int numberOfPeople) {
        if (numberOfPeople < 1) {
            throw new IllegalArgumentException("予約人数は1名以上を指定してください。");
        }

        WorkshopDTO workshop = workshopDAO.findById(workshopId);
        if (workshop == null) {
            throw new IllegalStateException("対象のワークショップが見つかりません。");
        }
        // コースの存在チェック(未選択・不正値を弾く)
        if (courseDAO.findById(courseId) == null) {
            throw new IllegalArgumentException("コースを選択してください。");
        }
        if (reservationDAO.existsReservation(memberId, workshopId)) {
            throw new IllegalStateException("このワークショップは既に予約済みです。");
        }
        if (workshop.getRemainingSeats() < numberOfPeople) {
            throw new IllegalStateException("空き枠が不足しています。(残席: " + workshop.getRemainingSeats() + ")");
        }

        ReservationDTO r = new ReservationDTO();
        r.setMemberId(memberId);
        r.setWorkshopId(workshopId);
        r.setCourseId(courseId);
        r.setNumberOfPeople(numberOfPeople);
        return reservationDAO.insert(r);
    }

    public List<ReservationDTO> findAll() {
        return reservationDAO.findAll();
    }

    public List<ReservationDTO> findByMemberId(int memberId) {
        return reservationDAO.findByMemberId(memberId);
    }

    /**
     * 会員自身による予約キャンセル。
     * DAO側で本人かつ確定状態の予約のみを対象にするため、ここでは
     * 結果が false(対象なし=他人の予約/既にキャンセル済み 等)のときに
     * 例外を送出して呼び出し元に通知する。
     * @throws IllegalStateException キャンセル対象が存在しない場合
     */
    public void cancel(int reservationId, int memberId) {
        boolean ok = reservationDAO.cancelByMember(reservationId, memberId);
        if (!ok) {
            throw new IllegalStateException("キャンセルできる予約が見つかりませんでした。");
        }
    }

    public ReservationDTO findById(int reservationId) {
        return reservationDAO.findById(reservationId);
    }

    /** 管理者による予約更新(人数・ステータス) */
    public boolean update(ReservationDTO r) {
        if (r.getNumberOfPeople() < 1) {
            throw new IllegalArgumentException("予約人数は1名以上を指定してください。");
        }
        return reservationDAO.update(r);
    }

    public boolean delete(int reservationId) {
        return reservationDAO.delete(reservationId);
    }
}
