<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>予約削除完了</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #dcdcdc;
            font-family: "Yu Gothic", "Hiragino Kaku Gothic ProN", sans-serif;
        }

        .container {
            width: 100%;
            height: 100vh; /* 画面全体の高さ */
            display: flex;
            flex-direction: column;
            justify-content: center; /* 縦方向中央 */
            align-items: center;     /* 横方向中央 */
        }

        .message {
            font-size: 52px;
            font-weight: bold;
            color: #000000;
            margin-bottom: 60px;
            text-align: center;
        }

        .button-form {
            margin: 0;
        }

        .top-button {
            width: 180px;
            height: 60px;
            border: 2px solid #2f4f6f;
            background-color: #eeeeee;
            font-size: 18px;
            cursor: pointer;
        }

        .top-button:hover {
            background-color: #dddddd;
        }
    </style>
</head>
<body>

    <div class="container">
        <div class="message">予約情報の削除を反映しました</div>

        <form action="topAdministrator.jsp" method="get" class="button-form">
            <button type="submit" class="top-button">トップに戻る</button>
        </form>
    </div>

</body>
</html>