package controller.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ReservationDao;
import dto.User;

@WebServlet("/user/reservation/cancel/complete")
public class ReservationCancelCompleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");

        if (user == null || user.getUser_id() == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String bookIdStr = request.getParameter("bookId");

        if (bookIdStr == null || bookIdStr.isEmpty()) {
            request.setAttribute("message", "キャンセル対象の予約IDが取得できませんでした。");
            request.getRequestDispatcher("/jsp/user/reservationComplete.jsp").forward(request, response);
            return;
        }

        Long bookId = Long.parseLong(bookIdStr);
        Long userId = user.getUser_id();

        ReservationDao dao = new ReservationDao();
        boolean result = dao.cancelReservation(bookId, userId);

        if (result) {
            request.setAttribute("message", "予約をキャンセルしました。");
        } else {
            request.setAttribute("message", "予約のキャンセルに失敗しました。対象の予約が見つからない可能性があります。");
        }

        request.getRequestDispatcher("/jsp/user/reservationCancelComplete.jsp").forward(request, response);
    }
}
