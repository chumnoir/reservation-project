package com.pottery.reservation.dto;

import java.sql.Timestamp;

/**
 * お知らせ(notices)テーブルに対応するデータ転送オブジェクト。
 */
public class NoticeDTO {

    private int noticeId;
    private String title;
    private String content;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public NoticeDTO() {
    }

    public int getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(int noticeId) {
        this.noticeId = noticeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
