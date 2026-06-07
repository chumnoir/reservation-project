<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>陶芸体験ワークショップ予約</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="landing">
    <div class="landing-box">
        <h1>陶芸体験ワークショップ</h1>
        <p class="lead">土にふれる、特別な時間を予約しよう。</p>
        <div class="landing-actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">ログイン</a>
            <a class="btn btn-outline" href="${pageContext.request.contextPath}/register">新規会員登録</a>
        </div>
        <%-- 管理者画面への導線は一般利用者ページには載せない。
             管理者は別URL(/admin/login)へ直接アクセスし、かつ許可IP(店舗端末)
             からのみ到達できる(AdminAuthFilterのIP許可リストで制御)。 --%>
        <p class="landing-admin">
            <a href="${pageContext.request.contextPath}/combined/">Service統合版（別構成のデモ）はこちら →</a>
        </p>
    </div>
</body>
</html>
