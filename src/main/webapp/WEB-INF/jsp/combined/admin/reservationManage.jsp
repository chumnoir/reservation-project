<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>予約管理 | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container">
    <h2 class="admin-title">予約管理</h2>

    <c:choose>
        <c:when test="${empty reservations}">
            <p class="empty">予約はありません。</p>
        </c:when>
        <c:otherwise>
            <table class="data-table">
                <thead>
                    <tr><th>ID</th><th>会員</th><th>ワークショップ</th><th>コース</th><th>開催日</th><th>人数</th><th>状態</th><th>予約日時</th><th>操作</th></tr>
                </thead>
                <tbody>
                    <c:forEach var="r" items="${reservations}">
                        <tr>
                            <td>${r.reservationId}</td>
                            <td>${r.memberName}<br><span class="sub">${r.memberEmail}</span></td>
                            <td>${r.workshopTitle}</td>
                            <td>${r.courseName}</td>
                            <td><fmt:formatDate value="${r.eventDate}" pattern="yyyy/MM/dd"/></td>
                            <td>${r.numberOfPeople} 名</td>
                            <td>
                                <c:choose>
                                    <c:when test="${r.status == 'CONFIRMED'}"><span class="badge badge-ok">確定</span></c:when>
                                    <c:otherwise><span class="badge badge-cancel">キャンセル</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td><fmt:formatDate value="${r.reservedAt}" pattern="yyyy/MM/dd HH:mm"/></td>
                            <td class="actions">
                                <a href="${pageContext.request.contextPath}/combined/admin/reservation?action=edit&id=${r.reservationId}"
                                   class="btn btn-sm btn-outline">編集</a>
                                <form action="${pageContext.request.contextPath}/combined/admin/reservation" method="post"
                                      onsubmit="return confirm('削除してよろしいですか？');" style="display:inline;">
                                    <input type="hidden" name="action" value="delete">
                                    <input type="hidden" name="reservationId" value="${r.reservationId}">
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
