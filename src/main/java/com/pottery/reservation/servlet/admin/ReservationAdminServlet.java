package com.pottery.reservation.servlet.admin;

import com.pottery.reservation.dto.ReservationDTO;
import com.pottery.reservation.service.ReservationService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 予約管理(/admin/reservation)を処理するServlet。
 * 編集(人数・ステータス)・削除機能を提供する(追加機能は持たない)。
 */
@WebServlet("/admin/reservation")
public class ReservationAdminServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("edit".equals(action)) {
            int id = parseInt(req.getParameter("id"), -1);
            req.setAttribute("reservation", reservationService.findById(id));
            req.getRequestDispatcher("/WEB-INF/jsp/admin/reservationForm.jsp").forward(req, resp);
            return;
        }
        req.setAttribute("reservations", reservationService.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/admin/reservationManage.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action == null ? "" : action) {
                case "update": {
                    ReservationDTO r = new ReservationDTO();
                    r.setReservationId(parseInt(req.getParameter("reservationId"), -1));
                    r.setNumberOfPeople(parseInt(req.getParameter("numberOfPeople"), 1));
                    r.setStatus(req.getParameter("status"));
                    reservationService.update(r);
                    break;
                }
                case "delete":
                    reservationService.delete(parseInt(req.getParameter("reservationId"), -1));
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/reservation");
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
