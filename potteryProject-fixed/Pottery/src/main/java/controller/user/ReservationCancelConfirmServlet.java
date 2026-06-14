package controller.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ReservationDao;
import dto.ReservationHistory;
import dto.User;

@WebServlet("/user/reservation/cancel/confirm")
public class ReservationCancelConfirmServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");

        if (user == null || user.getUser_id() == null) {
            response.sendRedirect("/jsp/user/login.jsp");
            return;
        }

        String bookIdStr = request.getParameter("bookId");
        Long bookId = Long.parseLong(bookIdStr);

        ReservationDao dao = new ReservationDao();

        // ▼ 1件だけ取得するメソッド（あとでDAOに追加）
        ReservationHistory history = dao.getHistoryByBookId(bookId, user.getUser_id());

        request.setAttribute("reservation", history);

        request.getRequestDispatcher("/jsp/user/reservationCancelConfirm.jsp")
               .forward(request, response);
    }
}
