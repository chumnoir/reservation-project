package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.NoticeDAO;
import com.pottery.reservation.dao.WorkshopDAO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;

/**
 * 【Service統合版】会員メインページ(/combined/user/main)。
 * 一覧取得とカレンダー用データ準備を doGet 内で直接行い、DAOを直接呼ぶ。
 */
@WebServlet("/combined/user/main")
public class CombinedMainServlet extends HttpServlet {

    private final WorkshopDAO workshopDAO = new WorkshopDAO();
    private final NoticeDAO noticeDAO = new NoticeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("workshops", workshopDAO.findAll());
        req.setAttribute("notices", noticeDAO.findAll());

        int year;
        int month;
        try {
            year = Integer.parseInt(req.getParameter("year"));
            month = Integer.parseInt(req.getParameter("month"));
        } catch (NumberFormatException e) {
            LocalDate today = LocalDate.now();
            year = today.getYear();
            month = today.getMonthValue();
        }
        YearMonth ym = YearMonth.of(year, month);

        req.setAttribute("calYear", ym.getYear());
        req.setAttribute("calMonth", ym.getMonthValue());
        req.setAttribute("daysInMonth", ym.lengthOfMonth());
        req.setAttribute("firstDayOffset", ym.atDay(1).getDayOfWeek().getValue() % 7);

        YearMonth prev = ym.minusMonths(1);
        YearMonth next = ym.plusMonths(1);
        req.setAttribute("prevYear", prev.getYear());
        req.setAttribute("prevMonth", prev.getMonthValue());
        req.setAttribute("nextYear", next.getYear());
        req.setAttribute("nextMonth", next.getMonthValue());

        req.getRequestDispatcher("/WEB-INF/jsp/combined/main.jsp").forward(req, resp);
    }
}
