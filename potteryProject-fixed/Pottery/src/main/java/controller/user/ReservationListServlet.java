package controller.user;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ReservationDao;
import dto.ReservationHistory;
import dto.User;

@WebServlet("/user/reservation/list")
public class ReservationListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");

            HttpSession session = request.getSession(false);

            if (session == null) {
                response.sendRedirect(request.getContextPath() + "/user/login");
                return;
            }

            User loginUser = (User) session.getAttribute("loginUser");

            if (loginUser == null || loginUser.getUser_id() == null) {
                response.sendRedirect(request.getContextPath() + "/user/login");
                return;
            }

            ReservationDao dao = new ReservationDao();
            List<ReservationHistory> reservationList =
                    dao.getHistoryByUserId(loginUser.getUser_id());

            request.setAttribute("reservationList", reservationList);

            request.getRequestDispatcher("/jsp/user/reservationList.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}