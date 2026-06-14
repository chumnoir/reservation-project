package controller.user;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.WorkShopDao;
import dto.WorkshopDisplay;

@WebServlet("/user/workshops")
public class WorkshopListServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            WorkShopDao workShopDao = new WorkShopDao();

            List<WorkshopDisplay> workshopList = workShopDao.findWorkshopDisplays();

            System.out.println("workshopList size = "
                    + (workshopList == null ? "null" : workshopList.size()));

            request.setAttribute("workshops", workshopList);

            request.getRequestDispatcher("/jsp/user/workshopList.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}