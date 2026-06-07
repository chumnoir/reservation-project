<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ログイン | 陶芸体験ワークショップ〔統合版〕</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth">
    <div class="auth-box">
        <h1>ログイン</h1>

        <c:if test="${param.registered == '1'}">
            <p class="alert alert-success">会員登録が完了しました。ログインしてください。</p>
        </c:if>
        <c:if test="${param.loggedout == '1'}">
            <p class="alert alert-success">ログアウトしました。</p>
        </c:if>
        <c:if test="${param.error == 'session'}">
            <p class="alert alert-error">ログインが必要です。</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="alert alert-error">${error}</p>
        </c:if>

        <form action="${pageContext.request.contextPath}/combined/login" method="post">
            <label>メールアドレス
                <input type="email" name="email" required>
            </label>
            <label>パスワード
                <input type="password" name="password" required>
            </label>
            <button type="submit" class="btn btn-primary btn-block">ログイン</button>
        </form>

        <p class="auth-link">
            アカウントをお持ちでない方は
            <a href="${pageContext.request.contextPath}/combined/register">会員登録</a>
        </p>
    </div>
</body>
</html>
