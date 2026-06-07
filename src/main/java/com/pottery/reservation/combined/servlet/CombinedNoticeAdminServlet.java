package com.pottery.reservation.combined.servlet;

import com.pottery.reservation.dao.NoticeDAO;
import com.pottery.reservation.dto.NoticeDTO;
import com.pottery.reservation.util.ValidationUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 【Service統合版】お知らせ管理(/combined/admin/notice)。
 * 検証・CRUDを doGet/doPost に直接記述し、NoticeDAO を直接呼ぶ。
 */
@WebServlet("/combined/admin/notice")
public class CombinedNoticeAdminServlet extends HttpServlet {

    private final NoticeDAO noticeDAO = new NoticeDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";
        switch (action) {
            case "new":
                req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/noticeForm.jsp").forward(req, resp);
                break;
            case "edit":
                req.setAttribute("notice", noticeDAO.findById(parseInt(req.getParameter("id"), -1)));
                req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/noticeForm.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("notices", noticeDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/combined/admin/noticeManage.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        switch (action == null ? "" : action) {
            case "insert": {
                NoticeDTO n = build(req);
                if (isValid(n)) noticeDAO.insert(n);
                break;
            }
            case "update": {
                NoticeDTO n = build(req);
                n.setNoticeId(parseInt(req.getParameter("noticeId"), -1));
                if (isValid(n)) noticeDAO.update(n);
                break;
            }
            case "delete":
                noticeDAO.delete(parseInt(req.getParameter("noticeId"), -1));
                break;
            default:
                break;
        }
        resp.sendRedirect(req.getContextPath() + "/combined/admin/notice");
    }

    private NoticeDTO build(HttpServletRequest req) {
        NoticeDTO n = new NoticeDTO();
        n.setTitle(req.getParameter("title"));
        n.setContent(req.getParameter("content"));
        return n;
    }

    private boolean isValid(NoticeDTO n) {
        return !ValidationUtil.isBlank(n.getTitle()) && !ValidationUtil.isBlank(n.getContent());
    }

    private int parseInt(String s, int def) {
        try { return Integer.parseInt(s); } catch (NumberFormatException e) { return def; }
    }
}
