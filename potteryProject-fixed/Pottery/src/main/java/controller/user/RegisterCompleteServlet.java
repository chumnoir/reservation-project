package controller.user;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UserDao;
import dto.User;

@WebServlet("/user/register/complete")
public class RegisterCompleteServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 値取得
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // DAO
        UserDao dao = new UserDao();

        // ✅ 重複チェック
        if (dao.existsByEmail(email)) {

            request.setAttribute("error", "このメールアドレスは既に登録されています");

            request.setAttribute("name", name);
            request.setAttribute("address", address);
            request.setAttribute("phone", phone);
            request.setAttribute("email", email);
            request.setAttribute("password", password);

            request.getRequestDispatcher("/jsp/user/RegisterConfirm.jsp")
                   .forward(request, response);
            return;
        }

        // DTO作成
        User user = new User();
        user.setUser_name(name);
        user.setAddress(address);
        user.setPhone_number(phone);
        user.setEmail(email);
        user.setPassword(password);

        // ✅ 登録処理
        boolean result = dao.insert(user);

        // ✅ 条件分岐（ここが今回のポイント🔥）
        if (result) {

            // 成功
            request.getSession().invalidate();

            response.sendRedirect(
                request.getContextPath() + "/jsp/user/RegisterComplete.jsp"
            );

        } else {

            // 失敗
            request.setAttribute("error", "登録に失敗しました");

            request.setAttribute("name", name);
            request.setAttribute("address", address);
            request.setAttribute("phone", phone);
            request.setAttribute("email", email);
            request.setAttribute("password", password);

            request.getRequestDispatcher("/jsp/user/userRegisterConfirm.jsp")
                   .forward(request, response);
        }
    }
}