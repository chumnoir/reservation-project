package com.pottery.reservation.servlet;

import com.pottery.reservation.dto.NoticeDTO;
import com.pottery.reservation.dto.WorkshopDTO;
import com.pottery.reservation.service.NoticeService;
import com.pottery.reservation.service.WorkshopService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * 会員メインページ(/user/main)を表示するServlet。
 * ワークショップ一覧・お知らせ一覧・カレンダー表示用データを準備する。
 */
@WebServlet("/user/main")
public class MainServlet extends HttpServlet {

    private final WorkshopService workshopService = new WorkshopService();
    private final NoticeService noticeService = new NoticeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // ワークショップ一覧
        List<WorkshopDTO> workshops = workshopService.findAll();
        // お知らせ一覧
        List<NoticeDTO> notices = noticeService.findAll();

        // カレンダー表示対象の年月(パラメータ未指定なら当月)
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

        req.setAttribute("workshops", workshops);
        req.setAttribute("notices", notices);
        req.setAttribute("calYear", ym.getYear());
        req.setAttribute("calMonth", ym.getMonthValue());
        req.setAttribute("daysInMonth", ym.lengthOfMonth());
        // 月初の曜日(1=月 ... 7=日)。カレンダーの先頭空白用に日曜始まりへ変換
        int firstDow = ym.atDay(1).getDayOfWeek().getValue() % 7; // 0=日,1=月,...6=土
        req.setAttribute("firstDayOffset", firstDow);

        // 前月・翌月
        YearMonth prev = ym.minusMonths(1);
        YearMonth next = ym.plusMonths(1);
        req.setAttribute("prevYear", prev.getYear());
        req.setAttribute("prevMonth", prev.getMonthValue());
        req.setAttribute("nextYear", next.getYear());
        req.setAttribute("nextMonth", next.getMonthValue());

        req.getRequestDispatcher("/WEB-INF/jsp/main.jsp").forward(req, resp);
    }
}
