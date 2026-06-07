package com.pottery.reservation.servlet.admin;

import com.pottery.reservation.service.MemberService;
import com.pottery.reservation.service.NoticeService;
import com.pottery.reservation.service.ReservationService;
import com.pottery.reservation.service.WorkshopService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理者ダッシュボード(/admin/dashboard)を表示するServlet。
 * 各管理画面(お知らせ/予約/ワークショップ/会員)への入口と件数サマリを表示する。
 */
@WebServlet("/admin/dashboard")
public class AdminDashboardServlet extends HttpServlet {

    private final WorkshopService workshopService = new WorkshopService();
    private final ReservationService reservationService = new ReservationService();
    private final NoticeService noticeService = new NoticeService();
    private final MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("workshopCount", workshopService.findAll().size());
        req.setAttribute("reservationCount", reservationService.findAll().size());
        req.setAttribute("noticeCount", noticeService.findAll().size());
        req.setAttribute("memberCount", memberService.findAll().size());
        req.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(req, resp);
    }
}
