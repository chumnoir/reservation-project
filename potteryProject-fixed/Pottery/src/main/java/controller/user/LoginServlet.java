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

@WebServlet("/user/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    /**
     * ログイン画面表示
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String redirect = normalizeRedirect(request.getParameter("redirect"));

        // login.jsp の hidden に渡す
        request.setAttribute("redirect", redirect);

        request.getRequestDispatcher("/jsp/user/login.jsp")
                .forward(request, response);
    }

    /**
     * ログイン処理
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String redirect = normalizeRedirect(request.getParameter("redirect"));

        if (email == null) {
            email = "";
        }

        if (password == null) {
            password = "";
        }

        email = email.trim();
        password = password.trim();

        if (email.isEmpty() || password.isEmpty()) {
            request.setAttribute("error", "メールアドレスとパスワードを入力してください。");
            request.setAttribute("email", email);
            request.setAttribute("redirect", redirect);

            request.getRequestDispatcher("/jsp/user/login.jsp")
                    .forward(request, response);
            return;
        }

        UserDao dao = new UserDao();
        User user = dao.findByLogin(email, password);

        if (user != null) {

            /*
             * セッション固定攻撃対策として、
             * 既存セッションがあれば破棄してから新しいセッションを作る。
             */
            HttpSession oldSession = request.getSession(false);
            if (oldSession != null) {
                oldSession.invalidate();
            }

            HttpSession session = request.getSession(true);
            session.setAttribute("loginUser", user);

            /*
             * redirect がある場合:
             *   ワークショップ一覧から「ログインして予約」を押したケース
             *
             * redirect がない場合:
             *   通常ログインなのでマイページへ
             */
            if (redirect != null && !redirect.isEmpty()) {
                response.sendRedirect(request.getContextPath() + redirect);
            } else {
                response.sendRedirect(request.getContextPath() + "/jsp/user/mypage.jsp");
            }

        } else {
            request.setAttribute("error", "メールアドレスまたはパスワードが違います。");
            request.setAttribute("email", email);
            request.setAttribute("redirect", redirect);

            request.getRequestDispatcher("/jsp/user/login.jsp")
                    .forward(request, response);
        }
    }

    /**
     * ログイン後の遷移先を安全に制限する。
     *
     * 許可する例:
     *   /user/reservation
     *
     * 拒否する例:
     *   http://example.com
     *   https://example.com
     *   //example.com
     */
    private String normalizeRedirect(String redirect) {

        if (redirect == null || redirect.isBlank()) {
            return null;
        }

        redirect = redirect.trim();

        // アプリ内パスだけ許可
        if (!redirect.startsWith("/")) {
            return null;
        }

        // 外部URL形式を拒否
        if (redirect.startsWith("//")) {
            return null;
        }

        if (redirect.contains("://")) {
            return null;
        }

        return redirect;
    }
}
