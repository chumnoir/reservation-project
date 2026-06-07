package com.pottery.reservation.service;

import com.pottery.reservation.dao.CourseDAO;
import com.pottery.reservation.dto.CourseDTO;

import java.util.List;

/**
 * コースに関する業務ロジックを担当するService。
 * 本演習ではマスタの参照のみ(一覧・ID検索)を提供する。
 */
public class CourseService {

    private final CourseDAO courseDAO = new CourseDAO();

    public List<CourseDTO> findAll() {
        return courseDAO.findAll();
    }

    public CourseDTO findById(int courseId) {
        return courseDAO.findById(courseId);
    }
}
