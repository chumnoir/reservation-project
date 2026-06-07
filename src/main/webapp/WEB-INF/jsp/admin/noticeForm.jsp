<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>お知らせ${empty notice ? '追加' : '編集'} | 管理画面</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="admin">
<%@ include file="_adminHeader.jspf" %>

<main class="admin-container narrow">
    <h2 class="admin-title">お知らせ${empty notice ? '追加' : '編集'}</h2>

    <form action="${pageContext.request.contextPath}/admin/notice" method="post" class="admin-form">
        <c:choose>
            <c:when test="${empty notice}">
                <input type="hidden" name="action" value="insert">
            </c:when>
            <c:otherwise>
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="noticeId" value="${notice.noticeId}">
            </c:otherwise>
        </c:choose>

        <label>タイトル
            <input type="text" name="title" value="${notice.title}" required>
        </label>
        <label>本文
            <textarea name="content" rows="8" required>${notice.content}</textarea>
        </label>

        <div class="form-actions">
            <a href="${pageContext.request.contextPath}/admin/notice" class="btn btn-outline">戻る</a>
            <button type="submit" class="btn btn-primary">保存する</button>
        </div>
    </form>
</main>
</body>
</html>
