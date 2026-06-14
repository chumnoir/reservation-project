package controller.user;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dto.User;

@WebServlet("/user/info/change/confirm")
public class UserinfoChangeConfirmServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/user/info/change");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

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

        String name = trim(request.getParameter("user_name"));
        String email = trim(request.getParameter("user_email"));
        String password = trim(request.getParameter("password"));
        String telephone = trim(request.getParameter("telephone"));
        String address = trim(request.getParameter("address"));

        if (isBlank(name)
                || isBlank(email)
                || isBlank(password)
                || isBlank(telephone)
                || isBlank(address)) {

            request.setAttribute("error", "未入力の項目があります。");

            request.setAttribute("user_name", name);
            request.setAttribute("user_email", email);
            request.setAttribute("password", password);
            request.setAttribute("telephone", telephone);
            request.setAttribute("address", address);

            RequestDispatcher rd = request.getRequestDispatcher("/jsp/user/userinfoChange.jsp");
            rd.forward(request, response);
            return;
        }

        User changeUser = new User();

        changeUser.setUser_id(loginUser.getUser_id());
        changeUser.setUser_name(name);
        changeUser.setEmail(email);
        changeUser.setPassword(password);
        changeUser.setPhone_number(telephone);
        changeUser.setAddress(address);

        session.setAttribute("changeUser", changeUser);

        request.getRequestDispatcher("/jsp/user/userinfoChangeConfirm.jsp")
                .forward(request, response);
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}