package controller.user;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.ReservationDao;
import dao.WorkShopDao;
import dto.Course;
import dto.Reservation;
import dto.User;

@WebServlet("/user/reservation/complete")
public class ReservationCompleteServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(request.getContextPath() + "/user/reservation");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            request.setCharacterEncoding("UTF-8");

            System.out.println("===== ReservationCompleteServlet doPost start =====");
            System.out.println("dateStr = " + request.getParameter("date"));
            System.out.println("timeStr = " + request.getParameter("time"));
            System.out.println("courseIdStr = " + request.getParameter("courseId"));
            System.out.println("name = " + request.getParameter("name"));
            System.out.println("numStr = " + request.getParameter("num"));

            HttpSession session = request.getSession(false);

            if (session == null) {
                System.out.println("NG: session is null");
                response.sendRedirect(request.getContextPath() + "/user/login");
                return;
            }

            User loginUser = (User) session.getAttribute("loginUser");

            if (loginUser == null) {
                System.out.println("NG: loginUser is null");
                response.sendRedirect(request.getContextPath() + "/user/login");
                return;
            }

            if (loginUser.getUser_id() == null) {
                System.out.println("NG: loginUser.getUser_id() is null");
                response.sendRedirect(request.getContextPath() + "/user/login");
                return;
            }

            int userId = Math.toIntExact(loginUser.getUser_id());
            System.out.println("userId = " + userId);

            String dateStr = trim(request.getParameter("date"));
            String timeStr = trim(request.getParameter("time"));
            String courseIdStr = trim(request.getParameter("courseId"));
            String name = trim(request.getParameter("name"));
            String numStr = trim(request.getParameter("num"));

            if (isBlank(dateStr)
                    || isBlank(timeStr)
                    || isBlank(courseIdStr)
                    || isBlank(name)
                    || isBlank(numStr)) {

                System.out.println("NG: blank parameter");
                response.sendRedirect(request.getContextPath() + "/user/reservation");
                return;
            }

            LocalDate date;
            LocalTime time;
            Long courseId;
            int num;

            try {
                date = LocalDate.parse(dateStr);
                time = LocalTime.parse(timeStr);
                courseId = Long.parseLong(courseIdStr);
                num = Integer.parseInt(numStr);

                System.out.println("parsed date = " + date);
                System.out.println("parsed time = " + time);
                System.out.println("parsed courseId = " + courseId);
                System.out.println("parsed num = " + num);

            } catch (DateTimeParseException | NumberFormatException e) {
                System.out.println("NG: parse error");
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/user/reservation");
                return;
            }

            if (num <= 0) {
                System.out.println("NG: num <= 0");
                response.sendRedirect(request.getContextPath() + "/user/reservation");
                return;
            }

            WorkShopDao workShopDao = new WorkShopDao();

            Integer workshopId = workShopDao.findWorkshopId(date, time, courseId);
            System.out.println("workshopId = " + workshopId);

            if (workshopId == null) {
                System.out.println("NG: workshopId is null");
                response.sendRedirect(request.getContextPath() + "/user/reservation");
                return;
            }

            int price = 0;
            List<Course> courseList = workShopDao.findCoursesByDateAndTime(date, time);

            System.out.println("courseList size = " + (courseList == null ? "null" : courseList.size()));

            if (courseList != null) {
                for (Course course : courseList) {
                    if (course != null) {
                        System.out.println("course.getCourse_id() = " + course.getCourse_id());
                        System.out.println("course.getPrice() = " + course.getPrice());

                        if (course.getCourse_id() != null
                                && course.getCourse_id().equals(courseId)) {
                            price = course.getPrice();
                            break;
                        }
                    }
                }
            }

            System.out.println("price = " + price);

            if (price <= 0) {
                System.out.println("NG: price <= 0");
                response.sendRedirect(request.getContextPath() + "/user/reservation");
                return;
            }

            int sumPrice = price * num;
            System.out.println("sumPrice = " + sumPrice);

            // 【改修】予約登録の前に、まず対象ワークショップの定員（残り枠数）を減らす。
            // decreaseCapacity は「capacity >= num」のときだけ更新するため、
            // 残り枠が足りなければ false が返り、満席として扱う。
            boolean isCapacityReduced = workShopDao.decreaseCapacity(workshopId, num);

            if (!isCapacityReduced) {
                System.out.println("NG: 定員オーバーまたは残枠不足により定員減算に失敗しました");

                // 満席のお知らせ画面へ
                request.getRequestDispatcher("/jsp/user/reservationFull.jsp")
                        .forward(request, response);
                return;
            }

            Reservation reservation = new Reservation(
                    null,
                    workshopId,
                    userId,
                    name,
                    num,
                    sumPrice
            );

            System.out.println("insert reservation:");
            System.out.println("workshop_id = " + reservation.getWorkshop_id());
            System.out.println("user_id = " + reservation.getUser_id());
            System.out.println("guest_id = " + reservation.getGuest_id());
            System.out.println("num_people = " + reservation.getNum_people());
            System.out.println("sum_price = " + reservation.getSum_price());

            ReservationDao reservationDao = new ReservationDao();
            boolean result = reservationDao.insert(reservation);

            System.out.println("insert result = " + result);

            if (!result) {
                System.out.println("NG: insert failed");
                // 予約登録に失敗したので、先に減らした定員を元に戻す（巻き戻し）
                workShopDao.increaseCapacity(workshopId, num);
                response.sendRedirect(request.getContextPath() + "/user/reservation");
                return;
            }

            System.out.println("OK: forward reservationComplete.jsp");

            request.getRequestDispatcher("/jsp/user/reservationComplete.jsp")
                    .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.isEmpty();
    }
}