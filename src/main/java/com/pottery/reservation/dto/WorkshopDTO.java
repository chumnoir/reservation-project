package com.pottery.reservation.dto;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * ワークショップ(workshops)テーブルに対応するデータ転送オブジェクト。
 */
public class WorkshopDTO {

    private int workshopId;
    private String title;
    private String description;
    private String instructor;
    private Date eventDate;       // 開催日
    private Time startTime;       // 開始時刻
    private int capacity;         // 定員
    private int price;            // 参加費
    private Timestamp createdAt;

    // 画面表示用: 現在の予約人数(JOIN集計で設定)
    private int reservedCount;

    public WorkshopDTO() {
    }

    public int getWorkshopId() {
        return workshopId;
    }

    public void setWorkshopId(int workshopId) {
        this.workshopId = workshopId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public int getReservedCount() {
        return reservedCount;
    }

    public void setReservedCount(int reservedCount) {
        this.reservedCount = reservedCount;
    }

    /** 残席数 */
    public int getRemainingSeats() {
        return capacity - reservedCount;
    }

    /** 満席かどうか */
    public boolean isFull() {
        return reservedCount >= capacity;
    }
}
