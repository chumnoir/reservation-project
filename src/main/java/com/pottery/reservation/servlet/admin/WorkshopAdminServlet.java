package com.pottery.reservation.servlet.admin;

import com.pottery.reservation.dto.WorkshopDTO;
import com.pottery.reservation.service.WorkshopService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

/**
 * ワークショップ管理(/admin/workshop)を処理するServlet。
 * 追加・編集・削除機能を提供する。
 */
@WebServlet("/admin/workshop")
public class WorkshopAdminServlet extends HttpServlet {

    private final WorkshopService workshopService = new WorkshopService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher("/WEB-INF/jsp/admin/workshopForm.jsp").forward(req, resp);
                break;
            case "edit":
                int id = parseInt(req.getParameter("id"), -1);
                req.setAttribute("workshop", workshopService.findById(id));
                req.getRequestDispatcher("/WEB-INF/jsp/admin/workshopForm.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("workshops", workshopService.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/admin/workshopManage.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action == null ? "" : action) {
                case "insert":
                    workshopService.create(buildWorkshop(req, false));
                    break;
                case "update":
                    workshopService.update(buildWorkshop(req, true));
                    break;
                case "delete":
                    workshopService.delete(parseInt(req.getParameter("workshopId"), -1));
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/workshop");
    }

    /** リクエストパラメータからWorkshopDTOを組み立てる */
    private WorkshopDTO buildWorkshop(HttpServletRequest req, boolean withId) {
        WorkshopDTO w = new WorkshopDTO();
        if (withId) {
            w.setWorkshopId(parseInt(req.getParameter("workshopId"), -1));
        }
        w.setTitle(req.getParameter("title"));
        w.setDescription(req.getParameter("description"));
        w.setInstructor(req.getParameter("instructor"));
        w.setEventDate(parseDate(req.getParameter("eventDate")));
        w.setStartTime(parseTime(req.getParameter("startTime")));
        w.setCapacity(parseInt(req.getParameter("capacity"), 0));
        w.setPrice(parseInt(req.getParameter("price"), 0));
        return w;
    }

    private Date parseDate(String s) {
        try {
            return Date.valueOf(s); // yyyy-MM-dd
        } catch (Exception e) {
            return null;
        }
    }

    private Time parseTime(String s) {
        try {
            // input type=time は "HH:mm" 形式 → "HH:mm:ss" に補完
            if (s != null && s.length() == 5) {
                s = s + ":00";
            }
            return Time.valueOf(s);
        } catch (Exception e) {
            return null;
        }
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
