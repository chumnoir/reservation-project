package com.pottery.reservation.dto;

import java.sql.Timestamp;

/**
 * コース(courses)テーブルに対応するデータ転送オブジェクト。
 * ワークショップ予約時に選択する体験コース(プラン)を表す。
 */
public class CourseDTO {

    private int courseId;
    private String name;
    private String description;
    private int price;          // コース追加料金(円)
    private Timestamp createdAt;

    public CourseDTO() {
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
}
