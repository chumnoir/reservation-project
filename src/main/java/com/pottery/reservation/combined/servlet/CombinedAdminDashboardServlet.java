package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.MemberDAO;
import com.pottery.reservation.dao.NoticeDAO;
import com.pottery.reservation.dao.ReservationDAO;
import com.pottery.reservation.dao.WorkshopDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 【Service統合版】管理者ダッシュボード(/combined/admin/dashboard)。
 * 各DAOを直接呼んで件数サマリを集計する。
 */
@WebServlet("/combined/admin/dashboard")
public class CombinedAdminDashboardServlet extends HttpServlet {

    private final WorkshopDAO workshopDAO = new WorkshopDAO();
    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final NoticeDAO noticeDAO = new NoticeDAO();
    private final MemberDAO memberDAO = new MemberDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("workshopCount", workshopDAO.findAll().size());
        req.setAttribute("reservationCount", reservationDAO.findAll().size());
        req.setAttribute("noticeCount", noticeDAO.findAll().size());
        req.setAttribute("memberCount", memberDAO.findAll().size());
        req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/dashboard.jsp").forward(req, resp);
    }
}
