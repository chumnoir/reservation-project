<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ワークショップ管理 | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container">
    <div class="admin-page-head">
        <h2 class="admin-title">ワークショップ管理</h2>
        <a href="${pageContext.request.contextPath}/combined/admin/workshop?action=new" class="btn btn-primary">＋ 新規追加</a>
    </div>

    <c:choose>
        <c:when test="${empty workshops}">
            <p class="empty">ワークショップはありません。</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>タイトル</th><th>開催日</th><th>時刻</th><th>定員</th><th>予約</th><th>参加費</th><th>操作</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="w" items="${workshops}">
                        <tr>
                            <td>${w.workshopId}</td>
                            <td>${w.title}</td>
                            <td><fmt:formatDate value="${w.eventDate}" pattern="yyyy/MM/dd"/></td>
                            <td><fmt:formatDate value="${w.startTime}" pattern="HH:mm"/></td>
                            <td>${w.capacity}</td>
                            <td>${w.reservedCount}</td>
                            <td><fmt:formatNumber value="${w.price}" type="number"/>円</td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/combined/admin/workshop?action=edit&id=${w.workshopId}"
                                   class="btn btn-sm btn-outline">編集</a>
                                <form action="${pageContext.request.contextPath}/combined/admin/workshop" method="post"
                                      onsubmit="return confirm('削除すると関連する予約も削除されます。よろしいですか？');" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="workshopId" value="${w.workshopId}">
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
