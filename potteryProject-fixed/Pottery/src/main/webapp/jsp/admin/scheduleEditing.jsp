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
%>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>予約編集</title>
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

        .form-area {
            width: 420px;
        }

        .input-box {
            width: 100%;
            height: 58px;
            margin-bottom: 30px;
            border: 2px solid #333;
            background-color: #ffffff;
            box-sizing: border-box;
            font-size: 18px;
            text-align: center;
            outline: none;
        }

        .input-box::placeholder {
            color: #333;
            opacity: 1;
        }

        /* ↓↓↓ 戻るボタンと保存ボタンの左右の空白を均等にする ↓↓↓ */
        .bottom-area {
            width: 620px;
            margin: 20px auto 0 auto;
            display: flex;
            justify-content: space-evenly;
            align-items: center;
        }

        .back-form,
        .save-form {
            margin: 0;
        }

        .back-button,
        .save-button {
            width: 220px;
            height: 62px;
            border: 2px solid #333;
            background-color: #eeeeee;
            font-size: 20px;
            font-weight: bold;
            cursor: pointer;
        }

        .back-button:hover,
        .save-button:hover {
            background-color: #dddddd;
        }
    </style>
</head>
<body>

<div class="page">

    <div class="form-area">
        <input type="text" id="reservationName" class="input-box"
               value="<%= reservationName %>" placeholder="予約者名">

        <input type="text" id="representativeName" class="input-box"
               value="<%= representativeName %>" placeholder="代表者名">

        <input type="text" id="reservationCourse" class="input-box"
               value="<%= reservationCourse %>" placeholder="予約コース">

        <input type="text" id="reservationDate" class="input-box"
               value="<%= reservationDate %>" placeholder="予約日付">

        <input type="text" id="reservationCount" class="input-box"
               value="<%= reservationCount %>" placeholder="予約人数">
    </div>

    <div class="bottom-area">
        <form action="reservationList.jsp" method="get" class="back-form">
            <button type="submit" class="back-button">戻る</button>
        </form>

        <form action="reservationEditConfiemation.jsp" method="post" class="save-form" onsubmit="setHiddenValues()">
            <input type="hidden" name="reservationName" id="hiddenReservationName">
            <input type="hidden" name="representativeName" id="hiddenRepresentativeName">
            <input type="hidden" name="reservationCourse" id="hiddenReservationCourse">
            <input type="hidden" name="reservationDate" id="hiddenReservationDate">
            <input type="hidden" name="reservationCount" id="hiddenReservationCount">

            <button type="submit" class="save-button">保存</button>
        </form>
    </div>

</div>

<script>
    function setHiddenValues() {
        document.getElementById("hiddenReservationName").value =
            document.getElementById("reservationName").value;

        document.getElementById("hiddenRepresentativeName").value =
            document.getElementById("representativeName").value;

        document.getElementById("hiddenReservationCourse").value =
            document.getElementById("reservationCourse").value;

        document.getElementById("hiddenReservationDate").value =
            document.getElementById("reservationDate").value;

        document.getElementById("hiddenReservationCount").value =
            document.getElementById("reservationCount").value;
    }
</script>

</body>
</html>