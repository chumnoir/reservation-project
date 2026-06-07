package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.WorkshopDAO;
import com.pottery.reservation.dto.WorkshopDTO;
import com.pottery.reservation.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;

/**
 * 【Service統合版】ワークショップ管理(/combined/admin/workshop)。
 * 検証・CRUD・日付/時刻のパースを doGet/doPost に直接記述する。
 */
@WebServlet("/combined/admin/workshop")
public class CombinedWorkshopAdminServlet extends HttpServlet {

    private final WorkshopDAO workshopDAO = new WorkshopDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new":
                req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/workshopForm.jsp").forward(req, resp);
                break;
            case "edit":
                req.setAttribute("workshop", workshopDAO.findById(parseInt(req.getParameter("id"), -1)));
                req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/workshopForm.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("workshops", workshopDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/workshopManage.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action == null ? "" : action) {
            case "insert": {
                WorkshopDTO w = build(req, false);
                if (isValid(w)) workshopDAO.insert(w);
                break;
            }
            case "update": {
                WorkshopDTO w = build(req, true);
                if (isValid(w)) workshopDAO.update(w);
                break;
            }
            case "delete":
                workshopDAO.delete(parseInt(req.getParameter("workshopId"), -1));
                break;
            default:
                break;
        }
        resp.sendRedirect(req.getContextPath() + "/combined/admin/workshop");
    }

    private WorkshopDTO build(HttpServletRequest req, boolean withId) {
        WorkshopDTO w = new WorkshopDTO();
        if (withId) w.setWorkshopId(parseInt(req.getParameter("workshopId"), -1));
        w.setTitle(req.getParameter("title"));
        w.setDescription(req.getParameter("description"));
        w.setInstructor(req.getParameter("instructor"));
        w.setEventDate(parseDate(req.getParameter("eventDate")));
        w.setStartTime(parseTime(req.getParameter("startTime")));
        w.setCapacity(parseInt(req.getParameter("capacity"), 0));
        w.setPrice(parseInt(req.getParameter("price"), 0));
        return w;
    }

    private boolean isValid(WorkshopDTO w) {
        return !ValidationUtil.isBlank(w.getTitle())
                && w.getEventDate() != null && w.getStartTime() != null
                && w.getCapacity() > 0 && w.getPrice() >= 0;
    }

    private Date parseDate(String s) {
        try { return Date.valueOf(s); } catch (Exception e) { return null; }
    }

    private Time parseTime(String s) {
        try {
            if (s != null && s.length() == 5) s = s + ":00";
            return Time.valueOf(s);
        } catch (Exception e) { return null; }
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }
}
