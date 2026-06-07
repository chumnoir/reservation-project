package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.ReservationDAO;
import com.pottery.reservation.dto.ReservationDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 【Service統合版】予約管理(/combined/admin/reservation)。
 * 編集(人数・ステータス)・削除を doGet/doPost に直接記述する。
 */
@WebServlet("/combined/admin/reservation")
public class CombinedReservationAdminServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        if ("edit".equals(req.getParameter("action"))) {
            req.setAttribute("reservation", reservationDAO.findById(parseInt(req.getParameter("id"), -1)));
            req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/reservationForm.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("reservations", reservationDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/reservationManage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action == null ? "" : action) {
            case "update": {
                int people = parseInt(req.getParameter("numberOfPeople"), 1);
                if (people >= 1) {
                    ReservationDTO r = new ReservationDTO();
                    r.setReservationId(parseInt(req.getParameter("reservationId"), -1));
                    r.setNumberOfPeople(people);
                    r.setStatus(req.getParameter("status"));
                    reservationDAO.update(r);
                }
                break;
            }
            case "delete":
                reservationDAO.delete(parseInt(req.getParameter("reservationId"), -1));
                break;
            default:
                break;
        }
        resp.sendRedirect(req.getContextPath() + "/combined/admin/reservation");
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }
}
