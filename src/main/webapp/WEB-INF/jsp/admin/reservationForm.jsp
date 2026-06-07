<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c"   uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>予約編集 | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container narrow">
    <h2 class="admin-title">予約編集</h2>

    <dl class="detail-list">
        <dt>会員</dt><dd>${reservation.memberName}（${reservation.memberEmail}）</dd>
        <dt>ワークショップ</dt><dd>${reservation.workshopTitle}</dd>
        <dt>コース</dt><dd>${reservation.courseName}</dd>
        <dt>開催日</dt><dd><fmt:formatDate value="${reservation.eventDate}" pattern="yyyy/MM/dd"/></dd>
    </dl>

    <form action="${pageContext.request.contextPath}/admin/reservation" method="post" class="admin-form">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="reservationId" value="${reservation.reservationId}">

        <label>予約人数
            <input type="number" name="numberOfPeople" value="${reservation.numberOfPeople}" min="1" required>
        </label>
        <label>状態
            <select name="status">
                <option value="CONFIRMED" ${reservation.status == 'CONFIRMED' ? 'selected' : ''}>予約確定</option>
                <option value="CANCELED"  ${reservation.status == 'CANCELED'  ? 'selected' : ''}>キャンセル</option>
            </select>
        </label>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/admin/reservation" class="btn btn-outline">戻る</a>
            <button type="submit" class="btn btn-primary">保存する</button>
        </div>
    </form>
</main>
</body>
</html>
