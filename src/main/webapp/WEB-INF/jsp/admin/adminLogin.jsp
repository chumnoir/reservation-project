<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理者ログイン</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body class="auth admin-auth">
    <div class="auth-box">
        <h1>管理者ログイン</h1>
        <p class="auth-sub">管理者専用ページです</p>

        <c:if test="${param.loggedout == '1'}">
            <p class="alert alert-success">ログアウトしました。</p>
        </c:if>
        <c:if test="${param.error == 'session'}">
            <p class="alert alert-error">管理者ログインが必要です。</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="alert alert-error">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/login" method="post">
            <label>メールアドレス
                <input type="email" name="email" required>
            </label>
            <label>パスワード
                <input type="password" name="password" required>
            </label>
            <button type="submit" class="btn btn-primary btn-block">ログイン</button>
        </form>

        <p class="auth-link"><a href="${pageContext.request.contextPath}/login">一般会員ログインはこちら</a></p>
    </div>
</body>
</html>
