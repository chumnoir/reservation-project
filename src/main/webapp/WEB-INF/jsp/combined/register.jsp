<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>会員登録 | 陶芸体験ワークショップ〔統合版〕</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body class="auth">
    <div class="auth-box">
        <h1>会員登録</h1>

        <c:if test="${not empty error}">
            <p class="alert alert-error">${error}</p>
        </c:if>

        <%-- HTML5 の required / pattern / minlength でブラウザ側でも一次チェックする
             (送信前バリデーション)。最終的な検証はサーバ側 MemberService で行う。 --%>
        <form action="${pageContext.request.contextPath}/combined/register" method="post">
            <label>氏名
                <input type="text" name="name" value="${member.name}" maxlength="100" required>
            </label>
            <label>メールアドレス
                <input type="email" name="email" value="${member.email}" maxlength="255" required>
            </label>
            <label>電話番号
                <input type="tel" name="phone" value="${member.phone}"
                       placeholder="090-1234-5678"
                       pattern="[0-9\-]{10,13}"
                       title="数字とハイフンで10〜13文字（例: 090-1234-5678）" required>
            </label>
            <label>住所
                <input type="text" name="address" value="${member.address}" maxlength="255" required>
            </label>
            <label>パスワード（6文字以上）
                <input type="password" name="password" minlength="6" maxlength="255" required>
            </label>
            <label>パスワード（確認）
                <input type="password" id="passwordConfirm" minlength="6" required>
            </label>
            <button type="submit" class="btn btn-primary btn-block">登録する</button>
        </form>

        <%-- パスワード一致チェック(クライアント側のみ。サーバには送信しない) --%>
        <script>
            document.querySelector('form').addEventListener('submit', function (e) {
                var pw = document.querySelector('input[name="password"]').value;
                var pwc = document.getElementById('passwordConfirm').value;
                if (pw !== pwc) {
                    e.preventDefault();
                    alert('パスワードが一致しません。');
                }
            });
        </script>

        <p class="auth-link">
            すでにアカウントをお持ちの方は
            <a href="${pageContext.request.contextPath}/combined/login">ログイン</a>
        </p>
    </div>
</body>
</html>
