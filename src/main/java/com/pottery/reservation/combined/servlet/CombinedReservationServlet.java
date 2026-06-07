package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.CourseDAO;
import com.pottery.reservation.dao.ReservationDAO;
import com.pottery.reservation.dao.WorkshopDAO;
import com.pottery.reservation.dto.MemberDTO;
import com.pottery.reservation.dto.ReservationDTO;
import com.pottery.reservation.dto.WorkshopDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 【Service統合版】予約(/combined/user/reserve)。
 *
 * 既存版では ReservationService が担っていた予約・キャンセルの業務ロジック
 * (重複チェック・定員チェック・コース存在チェック・本人確認)を、この
 * Servlet 内に直接記述する。DAO/DTO は共通部品として再利用する。
 */
@WebServlet("/combined/user/reserve")
public class CombinedReservationServlet extends HttpServlet {

    private final ReservationDAO reservationDAO = new ReservationDAO();
    private final WorkshopDAO workshopDAO = new WorkshopDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        MemberDTO member = (MemberDTO) req.getSession().getAttribute("cbLoginMember");

        if ("mylist".equals(action)) {
            req.setAttribute("reservations", reservationDAO.findByMemberId(member.getMemberId()));
            req.getRequestDispatcher("/WEB-INF/jsp/combined/myReservations.jsp").forward(req, resp);
            return;
        }

        int workshopId = parseInt(req.getParameter("id"), -1);
        WorkshopDTO workshop = workshopDAO.findById(workshopId);
        if (workshop == null) {
            resp.sendRedirect(req.getContextPath() + "/combined/user/main");
            return;
        }
        req.setAttribute("workshop", workshop);
        req.setAttribute("courses", courseDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/jsp/combined/reservation.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        MemberDTO member = (MemberDTO) session.getAttribute("cbLoginMember");
        String action = req.getParameter("action");

        // --- キャンセル(本人かつ確定状態のみ) ---
        if ("cancel".equals(action)) {
            int reservationId = parseInt(req.getParameter("reservationId"), -1);
            boolean ok = reservationDAO.cancelByMember(reservationId, member.getMemberId());
            session.setAttribute("flash", ok ? "予約をキャンセルしました。"
                                              : "キャンセルできる予約が見つかりませんでした。");
            resp.sendRedirect(req.getContextPath() + "/combined/user/reserve?action=mylist");
            return;
        }

        // --- 予約確定(業務ルールをインラインで検証) ---
        int workshopId = parseInt(req.getParameter("workshopId"), -1);
        int courseId = parseInt(req.getParameter("courseId"), -1);
        int people = parseInt(req.getParameter("numberOfPeople"), 1);

        String error = null;
        WorkshopDTO workshop = workshopDAO.findById(workshopId);
        if (people < 1) {
            error = "予約人数は1名以上を指定してください。";
        } else if (workshop == null) {
            error = "対象のワークショップが見つかりません。";
        } else if (courseDAO.findById(courseId) == null) {
            error = "コースを選択してください。";
        } else if (reservationDAO.existsReservation(member.getMemberId(), workshopId)) {
            error = "このワークショップは既に予約済みです。";
        } else if (workshop.getRemainingSeats() < people) {
            error = "空き枠が不足しています。(残席: " + workshop.getRemainingSeats() + ")";
        }

        if (error != null) {
            req.setAttribute("workshop", workshop);
            req.setAttribute("courses", courseDAO.findAll());
            req.setAttribute("error", error);
            req.getRequestDispatcher("/WEB-INF/jsp/combined/reservation.jsp").forward(req, resp);
            return;
        }

        ReservationDTO r = new ReservationDTO();
        r.setMemberId(member.getMemberId());
        r.setWorkshopId(workshopId);
        r.setCourseId(courseId);
        r.setNumberOfPeople(people);
        reservationDAO.insert(r);

        session.setAttribute("flash", "予約が完了しました。");
        resp.sendRedirect(req.getContextPath() + "/combined/user/reserve?action=mylist");
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
