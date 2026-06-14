<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String title = request.getParameter("title");
    String capacity = request.getParameter("capacity");
    String workshopDateTime = request.getParameter("workshopDateTime");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ワークショップ追加確認</title>

<style>
    body {
        margin: 0;
        padding: 0;
        font-family: "Yu Gothic", "Meiryo", sans-serif;
        background-color: #f4f4f4;
    }

    .page {
        width: 1100px;
        min-height: 700px;
        margin: 30px auto;
        padding: 40px 50px;
        background-color: #ffffff;
        border: 2px solid #333;
        box-sizing: border-box;
        position: relative;
    }

    .page-title {
        text-align: center;
        font-size: 34px;
        font-weight: bold;
        margin-bottom: 30px;
    }

    .message {
        text-align: center;
        font-size: 22px;
        margin-bottom: 35px;
    }

    .confirm-wrapper {
        width: 360px;
        margin: 0 auto;
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .confirm-group {
        display: flex;
        flex-direction: column;
        gap: 8px;
    }

    .confirm-label {
        font-size: 18px;
        font-weight: bold;
        text-align: center;
    }

    .confirm-value {
        width: 100%;
        min-height: 60px;
        line-height: 60px;
        border: 2px solid #3a5874;
        font-size: 22px;
        text-align: center;
        box-sizing: border-box;
        background-color: #fafafa;
        padding: 0 10px;
    }

    .button-area {
        width: 520px;
        margin: 80px auto 0 auto;
        display: flex;
        justify-content: center;
        gap: 30px;
    }

    .action-button {
        display: inline-block;
        width: 240px;
        height: 70px;
        line-height: 70px;
        text-align: center;
        text-decoration: none;
        border: 2px solid #3a5874;
        background-color: #fafafa;
        color: #000;
        font-size: 28px;
        cursor: pointer;
        box-sizing: border-box;
    }

    .action-button:hover {
        background-color: #e9ecef;
    }

    .back-area {
        position: absolute;
        left: 50px;
        bottom: 50px;
    }

    .back-button {
        display: inline-block;
        width: 240px;
        height: 90px;
        line-height: 90px;
        text-align: center;
        text-decoration: none;
        border: 2px solid #3a5874;
        background-color: #fafafa;
        color: #000;
        font-size: 32px;
        box-sizing: border-box;
    }

    .back-button:hover {
        background-color: #e9ecef;
    }
</style>
</head>
<body>

<div class="page">

    <div class="page-title">ワークショップ追加確認</div>

    <div class="message">以下の内容で保存してよろしいですか？</div>

    <div class="confirm-wrapper">

        <div class="confirm-group">
            <label class="confirm-label">コース名</label>
            <div class="confirm-value"><%= title %></div>
        </div>

        <div class="confirm-group">
            <label class="confirm-label">定員</label>
            <div class="confirm-value"><%= capacity %></div>
        </div>

        <div class="confirm-group">
            <label class="confirm-label">開催日時</label>
            <div class="confirm-value"><%= workshopDateTime %></div>
        </div>

    </div>

    workshopAddComplete
        <input type="hidden" name="title" value="<%= title %>">
        <input type="hidden" name="capacity" value="<%= capacity %>">
        <input type="hidden" name="workshopDateTime" value="<%= workshopDateTime %>">

        <div class="button-area">
            <button type="submit" class="action-button">保存</button>
        </div>
    </form>

    <div class="back-area">
        <a class="back-button" href="workshopAdd.jsp">戻る</a>
    </div>

</div>

</body>
</html>