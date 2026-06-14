<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>管理者ログイン</title>
    <style>
        body {
            font-family: Arial;
            background-color: #f5f5f5;
        }
        .login-box {
            width: 300px;
            margin: 100px auto;
            padding: 20px;
            background: white;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        input {
            width: 100%;
            padding: 10px;
            margin: 8px 0;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #007BFF;
            color: white;
            border: none;
        }
        .error {
            color: red;
        }
    </style>
</head>
<body>

<div class="login-box">
    <h2>管理者ログイン</h2>

    <!-- エラーメッセージ表示 -->
    <%
        String error = (String) request.getAttribute("error");
        if (error != null) {
    %>
        <p class="error"><%= error %></p>
    <%
        }
    %>

    <!-- ログインフォーム -->
    <form action="<%=request.getContextPath()%>/AdminServlet" method="post">
        <label>メールアドレス</label>
        <input type="email" name="email" required>

        <label>パスワード</label>
        <input type="password" name="password" required>

        <button type="submit">ログイン</button>
    </form>
</div>

</body>
</html>
``