package com.pottery.reservation.service;

import com.pottery.reservation.dao.WorkshopDAO;
import com.pottery.reservation.dto.WorkshopDTO;

import java.util.List;

/**
 * ワークショップに関する業務ロジックを担当するService。
 */
public class WorkshopService {

    private final WorkshopDAO workshopDAO = new WorkshopDAO();

    public List<WorkshopDTO> findAll() {
        return workshopDAO.findAll();
    }

    public WorkshopDTO findById(int workshopId) {
        return workshopDAO.findById(workshopId);
    }

    public boolean create(WorkshopDTO w) {
        validate(w);
        return workshopDAO.insert(w);
    }

    public boolean update(WorkshopDTO w) {
        validate(w);
        return workshopDAO.update(w);
    }

    public boolean delete(int workshopId) {
        return workshopDAO.delete(workshopId);
    }

    private void validate(WorkshopDTO w) {
        if (w.getTitle() == null || w.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("ワークショップ名を入力してください。");
        }
        if (w.getEventDate() == null) {
            throw new IllegalArgumentException("開催日を入力してください。");
        }
        if (w.getStartTime() == null) {
            throw new IllegalArgumentException("開始時刻を入力してください。");
        }
        if (w.getCapacity() <= 0) {
            throw new IllegalArgumentException("定員は1以上で入力してください。");
        }
        if (w.getPrice() < 0) {
            throw new IllegalArgumentException("参加費は0以上で入力してください。");
        }
    }
}
