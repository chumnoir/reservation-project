<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>お知らせ管理 | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container">
    <div class="admin-page-head">
        <h2 class="admin-title">お知らせ管理</h2>
        <a href="${pageContext.request.contextPath}/admin/notice?action=new" class="btn btn-primary">＋ 新規追加</a>
    </div>

    <c:choose>
        <c:when test="${empty notices}">
            <p class="empty">お知らせはありません。</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>タイトル</th><th>作成日</th><th>操作</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="n" items="${notices}">
                        <tr>
                            <td>${n.noticeId}</td>
                            <td>${n.title}</td>
                            <td><fmt:formatDate value="${n.createdAt}" pattern="yyyy/MM/dd HH:mm"/></td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/admin/notice?action=edit&id=${n.noticeId}"
                                   class="btn btn-sm btn-outline">編集</a>
                                <form action="${pageContext.request.contextPath}/admin/notice" method="post"
                                      onsubmit="return confirm('削除してよろしいですか？');" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="noticeId" value="${n.noticeId}">
                                    <button type="submit" class="btn btn-sm btn-danger">削除</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
