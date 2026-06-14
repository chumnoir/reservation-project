package controller.user;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class UserLogoutServlet
 */
@WebServlet("/user/logout")
public class LogoutServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        // セッション削除（既存セッションがある場合のみ）
        jakarta.servlet.http.HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // トップに戻る（自分自身ではなくトップページへ。以前は /user/logout を指していてリダイレクトが無限ループしていた）
        response.sendRedirect(request.getContextPath() + "/user/main");
    }
}