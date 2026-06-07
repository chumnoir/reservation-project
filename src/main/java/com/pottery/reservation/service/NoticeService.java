package com.pottery.reservation.service;

import com.pottery.reservation.dao.NoticeDAO;
import com.pottery.reservation.dto.NoticeDTO;

import java.util.List;

/**
 * お知らせに関する業務ロジックを担当するService。
 */
public class NoticeService {

    private final NoticeDAO noticeDAO = new NoticeDAO();

    public List<NoticeDTO> findAll() {
        return noticeDAO.findAll();
    }

    public NoticeDTO findById(int noticeId) {
        return noticeDAO.findById(noticeId);
    }

    public boolean create(NoticeDTO n) {
        validate(n);
        return noticeDAO.insert(n);
    }

    public boolean update(NoticeDTO n) {
        validate(n);
        return noticeDAO.update(n);
    }

    public boolean delete(int noticeId) {
        return noticeDAO.delete(noticeId);
    }

    private void validate(NoticeDTO n) {
        if (n.getTitle() == null || n.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("タイトルを入力してください。");
        }
        if (n.getContent() == null || n.getContent().trim().isEmpty()) {
            throw new IllegalArgumentException("本文を入力してください。");
        }
    }
}
