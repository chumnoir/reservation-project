<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ワークショップ${empty workshop ? '追加' : '編集'} | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container narrow">
    <h2 class="admin-title">ワークショップ${empty workshop ? '追加' : '編集'}</h2>

    <%-- 編集時の日付・時刻初期値 --%>
    <c:set var="evDate"><fmt:formatDate value="${workshop.eventDate}" pattern="yyyy-MM-dd"/></c:set>
    <c:set var="stTime"><fmt:formatDate value="${workshop.startTime}" pattern="HH:mm"/></c:set>

    <form action="${pageContext.request.contextPath}/combined/admin/workshop" method="post" class="admin-form">
        <c:choose>
            <c:when test="${empty workshop}">
                <input type="hidden" name="action" value="insert">
            </c:when>
            <c:otherwise>
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="workshopId" value="${workshop.workshopId}">
            </c:otherwise>
        </c:choose>

        <label>ワークショップ名
            <input type="text" name="title" value="${workshop.title}" required>
        </label>
        <label>説明
            <textarea name="description" rows="5" required>${workshop.description}</textarea>
        </label>
        <label>講師名
            <input type="text" name="instructor" value="${workshop.instructor}" required>
        </label>
        <div class="form-row">
            <label>開催日
                <input type="date" name="eventDate" value="${evDate}" required>
            </label>
            <label>開始時刻
                <input type="time" name="startTime" value="${stTime}" required>
            </label>
        </div>
        <div class="form-row">
            <label>定員
                <input type="number" name="capacity" value="${empty workshop ? 10 : workshop.capacity}" min="1" required>
            </label>
            <label>参加費（円）
                <input type="number" name="price" value="${empty workshop ? 0 : workshop.price}" min="0" required>
            </label>
        </div>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/combined/admin/workshop" class="btn btn-outline">戻る</a>
            <button type="submit" class="btn btn-primary">保存する</button>
        </div>
    </form>
</main>
</body>
</html>
