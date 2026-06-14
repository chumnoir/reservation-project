package controller.user;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.UserDao;
import dto.User;

@WebServlet("/user/info/change/complete")
public class UserinfoChangeCompleteServlet extends HttpServlet {

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

        User changeUser = (User) session.getAttribute("changeUser");

        if (changeUser == null || changeUser.getUser_id() == null) {
            request.setAttribute("error", "変更情報が見つかりません。もう一度入力してください。");
            request.getRequestDispatcher("/jsp/user/userinfoChange.jsp")
                    .forward(request, response);
            return;
        }

        UserDao dao = new UserDao();
        boolean result = dao.update(changeUser);

        if (result) {
            session.setAttribute("loginUser", changeUser);
            session.removeAttribute("changeUser");

            request.getRequestDispatcher("/jsp/user/userinfoChangeComplete.jsp")
                    .forward(request, response);

        } else {
            request.setAttribute("error", "ユーザー情報の変更に失敗しました。");
            request.getRequestDispatcher("/jsp/user/userinfoChange.jsp")
                    .forward(request, response);
        }
    }
}