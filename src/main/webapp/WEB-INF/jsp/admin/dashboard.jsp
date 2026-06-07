<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ダッシュボード | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container">
    <h2 class="admin-title">ダッシュボード</h2>
    <p class="admin-lead">各管理メニューから情報の追加・編集・削除を行えます。</p>

    <div class="stat-grid">
        <div class="stat-card">
            <span class="stat-num">${noticeCount}</span>
            <span class="stat-label">お知らせ</span>
            <a href="${pageContext.request.contextPath}/admin/notice" class="btn btn-sm btn-primary">管理する</a>
        </div>
        <div class="stat-card">
            <span class="stat-num">${workshopCount}</span>
            <span class="stat-label">ワークショップ</span>
            <a href="${pageContext.request.contextPath}/admin/workshop" class="btn btn-sm btn-primary">管理する</a>
        </div>
        <div class="stat-card">
            <span class="stat-num">${reservationCount}</span>
            <span class="stat-label">予約</span>
            <a href="${pageContext.request.contextPath}/admin/reservation" class="btn btn-sm btn-primary">管理する</a>
        </div>
        <div class="stat-card">
            <span class="stat-num">${memberCount}</span>
            <span class="stat-label">会員</span>
            <a href="${pageContext.request.contextPath}/admin/member" class="btn btn-sm btn-primary">管理する</a>
        </div>
    </div>
</main>
</body>
</html>
