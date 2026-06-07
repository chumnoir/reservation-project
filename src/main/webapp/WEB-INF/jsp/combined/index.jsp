<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>陶芸体験ワークショップ〔統合版〕予約</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="landing">
    <div class="landing-box">
        <h1>陶芸体験ワークショップ〔統合版〕</h1>
        <p class="lead">土にふれる、特別な時間を予約しよう。</p>
        <div class="landing-actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/combined/login">ログイン</a>
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/combined/register">新規会員登録</a>
        </div>
        <p class="lead" style="font-size:13px;color:#8c4a13;">※これは Service層をServletに統合した別構成のデモです。</p>
        <%-- 管理者画面への導線は一般利用者ページには載せない。 --%>
        <p class="landing-admin">
            <a href="${pageContext.request.contextPath}/">← 通常版（MVC分離）へ戻る</a>
        </p>
    </div>
</body>
</html>
