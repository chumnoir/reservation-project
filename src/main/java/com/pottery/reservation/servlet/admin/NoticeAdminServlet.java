package com.pottery.reservation.servlet.admin;

import com.pottery.reservation.dto.NoticeDTO;
import com.pottery.reservation.service.NoticeService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * お知らせ管理(/admin/notice)を処理するServlet。
 * 追加・編集・削除機能を提供する。
 *   GET  action=list(既定) / new / edit&id=
 *   POST action=insert / update / delete
 */
@WebServlet("/admin/notice")
public class NoticeAdminServlet extends HttpServlet {

    private final NoticeService noticeService = new NoticeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "new":
                req.getRequestDispatcher("/WEB-INF/jsp/admin/noticeForm.jsp").forward(req, resp);
                break;
            case "edit":
                int id = parseInt(req.getParameter("id"), -1);
                NoticeDTO notice = noticeService.findById(id);
                req.setAttribute("notice", notice);
                req.getRequestDispatcher("/WEB-INF/jsp/admin/noticeForm.jsp").forward(req, resp);
                break;
            default:
                req.setAttribute("notices", noticeService.findAll());
                req.getRequestDispatcher("/WEB-INF/jsp/admin/noticeManage.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            switch (action == null ? "" : action) {
                case "insert": {
                    NoticeDTO n = new NoticeDTO();
                    n.setTitle(req.getParameter("title"));
                    n.setContent(req.getParameter("content"));
                    noticeService.create(n);
                    break;
                }
                case "update": {
                    NoticeDTO n = new NoticeDTO();
                    n.setNoticeId(parseInt(req.getParameter("noticeId"), -1));
                    n.setTitle(req.getParameter("title"));
                    n.setContent(req.getParameter("content"));
                    noticeService.update(n);
                    break;
                }
                case "delete":
                    noticeService.delete(parseInt(req.getParameter("noticeId"), -1));
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            req.setAttribute("error", e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + "/admin/notice");
    }

    private int parseInt(String s, int def) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
