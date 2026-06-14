package controller.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dto.User;

@WebServlet("/user/info/change")
public class UserinfoChangeServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        request.setAttribute("loginUser", loginUser);

        request.getRequestDispatcher("/jsp/user/userinfoChange.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        doGet(request, response);
    }
}