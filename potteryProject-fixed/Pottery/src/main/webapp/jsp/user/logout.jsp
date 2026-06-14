<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ログアウト</title>

<style>
body {
    margin: 0;
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    font-family: sans-serif;
}

/* メイン */
.content {
    flex: 1;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}

/* ボタン */
button {
    padding: 10px 30px;
    margin-top: 20px;
}

/* フッター */
footer {
    background-color: #ccc;
    text-align: center;
    padding: 10px;
    border-top: 1px solid #000;
}
</style>

</head>
<body>

<!-- ✅ ヘッダー読み込み -->
<jsp:include page="/jsp/header.jsp" />

<!-- ✅ メイン -->
<div class="content">

    <p>ログアウトしました。</p>

    <form action="<%=request.getContextPath()%>/top" method="get">
        <button type="submit">トップへ</button>
    </form>

</div>

<!-- ✅ フッター -->
<footer>
    フッター
</footer>

</body>
</html>