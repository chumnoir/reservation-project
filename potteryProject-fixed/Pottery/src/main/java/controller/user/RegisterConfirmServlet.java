package controller.user;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/user/register/confirm")
public class RegisterConfirmServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 値取得
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone_number");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // ✅ デバッグ（最強）
        System.out.println("name=" + name);

        // ✅ session保存（戻る用）
        HttpSession session = request.getSession();
        session.setAttribute("name", name);
        session.setAttribute("address", address);
        session.setAttribute("phone", phone);
        session.setAttribute("email", email);
        session.setAttribute("password", password);
        session.setAttribute("fromConfirm", true);

        // ✅ request（表示用）
        request.setAttribute("name", name);
        request.setAttribute("address", address);
        request.setAttribute("phone", phone);
        request.setAttribute("email", email);
        request.setAttribute("password", password);

        // confirmへ
        request.getRequestDispatcher("/jsp/user/RegisterConfirm.jsp")
               .forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();

        Boolean fromConfirm = (Boolean) session.getAttribute("fromConfirm");

        if (fromConfirm != null && fromConfirm) {

            request.setAttribute("name", session.getAttribute("name"));
            request.setAttribute("address", session.getAttribute("address"));
            request.setAttribute("phone", session.getAttribute("phone"));
            request.setAttribute("email", session.getAttribute("email"));

            session.removeAttribute("fromConfirm");
        }

        request.getRequestDispatcher("/jsp/user/Register.jsp")
               .forward(request, response);
    }
}