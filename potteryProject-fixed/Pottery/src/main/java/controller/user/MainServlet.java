package controller.user;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.InfoDao;
import dto.Info;

@WebServlet("/user/main")
public class MainServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            InfoDao infoDao = new InfoDao();
            List<Info> infoList = infoDao.findAll();

            request.setAttribute("infos", infoList);

            RequestDispatcher rd = request.getRequestDispatcher("/jsp/user/main.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}