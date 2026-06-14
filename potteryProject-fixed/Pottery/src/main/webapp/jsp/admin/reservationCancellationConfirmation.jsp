<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");
    String selectedReservation = request.getParameter("selectedReservation");

    if (selectedReservation == null || selectedReservation.isEmpty()) {
        selectedReservation = "予約情報の詳細を表示";
    }
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>予約削除確認</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #dcdcdc;
            font-family: "Yu Gothic", "Hiragino Kaku Gothic ProN", sans-serif;
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 40px auto;
            text-align: center;
        }

        .title {
            font-size: 32px;
            font-weight: bold;
            margin-bottom: 30px;
            color: #000;
        }

        .detail-box {
            width: 680px;
            min-height: 260px;
            margin: 0 auto 30px auto;
            border: 2px solid rgb(0, 0, 0);

            /* ↓↓↓ ここを変更：四角の中の背景色を白にした ↓↓↓ */
            background-color: #ffffff;

            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 20px;
            box-sizing: border-box;
            padding: 20px;
            text-align: center;
            white-space: normal;
        }

        .button-area {
            width: 500px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
        }

        .button-form {
            margin: 0;
        }

        .action-button {
            width: 180px;
            height: 65px;
            border: 2px solid #2f4f6f;
            background-color: #dcdcdc;
            font-size: 24px;
            font-weight: bold;
            cursor: pointer;
        }

        .action-button:hover {
            background-color: #cfcfcf;
        }
    </style>
</head>
<body>

    <div class="container">
        <div class="title">以下の予約を削除しますか？</div>

        <div class="detail-box"><%= selectedReservation %></div>

        <div class="button-area">
            <form action="reservationCancellationConfirmed.jsp" method="post" class="button-form">
                <input type="hidden" name="selectedReservation" value="<%= selectedReservation %>">
                <button type="submit" class="action-button">はい</button>
            </form>

            <form action="reservationList.jsp" method="get" class="button-form">
                <button type="submit" class="action-button">いいえ</button>
            </form>
        </div>
    </div>

</body>
</html>