<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    request.setCharacterEncoding("UTF-8");

    String reservationName = request.getParameter("reservationName");
    String representativeName = request.getParameter("representativeName");
    String reservationCourse = request.getParameter("reservationCourse");
    String reservationDate = request.getParameter("reservationDate");
    String reservationCount = request.getParameter("reservationCount");

    if (reservationName == null) reservationName = "";
    if (representativeName == null) representativeName = "";
    if (reservationCourse == null) reservationCourse = "";
    if (reservationDate == null) reservationDate = "";
    if (reservationCount == null) reservationCount = "";

    String detailText =
            "予約者名：" + reservationName + "\n" +
            "代表者名：" + representativeName + "\n" +
            "予約コース：" + reservationCourse + "\n" +
            "予約日付：" + reservationDate + "\n" +
            "予約人数：" + reservationCount;
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>予約編集確認</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #dcdcdc;
            font-family: "Yu Gothic", "Hiragino Kaku Gothic ProN", sans-serif;
        }

        .page {
            width: 100%;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            box-sizing: border-box;
        }

        .title {
            font-size: 52px;
            font-weight: bold;
            margin-bottom: 25px;
            color: #000000;
            text-align: center;
        }

        .detail-box {
            width: 680px;
            min-height: 280px;
            border: 2px solid #2f4f6f;
            background-color: #ffffff;
            box-sizing: border-box;
            padding: 30px;
            font-size: 24px;
            line-height: 1.8;
            white-space: pre-wrap;
            display: flex;
            justify-content: center;
            align-items: center;
            text-align: center;
            margin-bottom: 35px;
        }

        .button-area {
            width: 470px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .button-form {
            margin: 0;
        }

        .action-button {
            width: 180px;
            height: 64px;
            border: 2px solid #2f4f6f;
            background-color: #eeeeee;
            font-size: 20px;
            font-weight: bold;
            cursor: pointer;
        }

        .action-button:hover {
            background-color: #dddddd;
        }
    </style>
</head>
<body>

<div class="page">
    <div class="title">以下の内容で確定しますか？</div>

    <div class="detail-box"><%= detailText %></div>

    <div class="button-area">
        <!-- はい：確定画面へ -->
        <form action="scheduledEditingConfiemed.jsp" method="post" class="button-form">
            <input type="hidden" name="reservationName" value="<%= reservationName %>">
            <input type="hidden" name="representativeName" value="<%= representativeName %>">
            <input type="hidden" name="reservationCourse" value="<%= reservationCourse %>">
            <input type="hidden" name="reservationDate" value="<%= reservationDate %>">
            <input type="hidden" name="reservationCount" value="<%= reservationCount %>">
            <button type="submit" class="action-button">はい</button>
        </form>

        <!-- いいえ：編集画面へ戻る -->
        <form action="scheduleEditing.jsp" method="post" class="button-form">
            <input type="hidden" name="reservationName" value="<%= reservationName %>">
            <input type="hidden" name="representativeName" value="<%= representativeName %>">
            <input type="hidden" name="reservationCourse" value="<%= reservationCourse %>">
            <input type="hidden" name="reservationDate" value="<%= reservationDate %>">
            <input type="hidden" name="reservationCount" value="<%= reservationCount %>">
            <button type="submit" class="action-button">いいえ</button>
        </form>
    </div>
</div>

</body>
</html>