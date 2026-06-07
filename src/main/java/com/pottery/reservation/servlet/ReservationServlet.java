package com.pottery.reservation.servlet;

import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.dto.ReservationDTO;
import com.pottery.reservation.dto.WorkshopDTO;
import com.pottery.reservation.service.CourseService;
import com.pottery.reservation.service.ReservationService;
import com.pottery.reservation.service.WorkshopService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 会員によるワークショップ予約(/user/reserve)を処理するServlet。
 * カレンダー・一覧の双方からこのServletへ遷移して予約できる。
 *   GET ?id=  : 予約確認画面
 *   GET action=mylist : 自分の予約一覧
 *   POST      : 予約確定
 */
@WebServlet("/user/reserve")
public class ReservationServlet extends HttpServlet {

    private final ReservationService reservationService = new ReservationService();
    private final WorkshopService workshopService = new WorkshopService();
    private final CourseService courseService = new CourseService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        MemberDTO member = (MemberDTO) req.getSession().getAttribute("loginMember");

        if ("mylist".equals(action)) {
            // マイ予約一覧
            List<ReservationDTO> myReservations = reservationService.findByMemberId(member.getMemberId());
            req.setAttribute("reservations", myReservations);
            req.getRequestDispatcher("/WEB-INF/jsp/myReservations.jsp").forward(req, resp);
            return;
        }

        // 予約確認画面
        int workshopId = parseInt(req.getParameter("id"), -1);
        WorkshopDTO workshop = workshopService.findById(workshopId);
        if (workshop == null) {
            resp.sendRedirect(req.getContextPath() + "/user/main");
            return;
        }
        req.setAttribute("workshop", workshop);
        // 予約時に選択できるコース一覧を渡す
        req.setAttribute("courses", courseService.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/reservation.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("loginMember");
        String action = req.getParameter("action");

        // --- 予約キャンセル(マイ予約画面から呼ばれる) ---
        if ("cancel".equals(action)) {
            int reservationId = parseInt(req.getParameter("reservationId"), -1);
            try {
                // 本人の確定予約のみキャンセルできる(Service/DAOで保証)
                reservationService.cancel(reservationId, member.getMemberId());
                session.setAttribute("flash", "予約をキャンセルしました。");
            } catch (IllegalStateException e) {
                session.setAttribute("flash", e.getMessage());
            }
            resp.sendRedirect(req.getContextPath() + "/user/reserve?action=mylist");
            return;
        }

        // --- 予約確定 ---
        int workshopId = parseInt(req.getParameter("workshopId"), -1);
        int courseId = parseInt(req.getParameter("courseId"), -1);
        int people = parseInt(req.getParameter("numberOfPeople"), 1);

        try {
            reservationService.reserve(member.getMemberId(), workshopId, courseId, people);
            session.setAttribute("flash", "予約が完了しました。");
            resp.sendRedirect(req.getContextPath() + "/user/reserve?action=mylist");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // エラー時は確認画面を再表示(ワークショップ・コース一覧を再設定)
            WorkshopDTO workshop = workshopService.findById(workshopId);
            req.setAttribute("workshop", workshop);
            req.setAttribute("courses", courseService.findAll());
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/jsp/reservation.jsp").forward(req, resp);
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
