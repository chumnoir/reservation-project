<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.List" %>
<%@ page import="dto.WorkShop" %>

<%
    List<WorkShop> list = (List<WorkShop>) request.getAttribute("list");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ワークショップ一覧</title>

<style>
    body {
        margin: 0;
        padding: 0;
        font-family: "Yu Gothic", "Meiryo", sans-serif;
        background-color: #f5f5f5;
    }

    .page {
        width: 1000px;
        margin: 30px auto;
        padding: 30px;
        border: 2px solid #333;
        background-color: #fff;
        box-sizing: border-box;
    }

    .list-area {
        width: 850px;
        min-height: 350px;
        margin: 0 auto 40px auto;
        border: 2px solid #333;
        padding: 20px;
        box-sizing: border-box;
    }

    .list-title {
        text-align: center;
        font-size: 28px;
        margin-bottom: 20px;
        font-weight: bold;
    }

    table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 10px;
    }

    th, td {
        border: 1px solid #333;
        padding: 10px;
        text-align: center;
    }

    th {
        background-color: #e8e8e8;
    }

    .button-area {
        display: flex;
        justify-content: center;
        gap: 40px;
    }

    .menu-button {
        display: inline-block;
        width: 220px;
        height: 70px;
        line-height: 70px;
        text-align: center;
        text-decoration: none;
        border: 2px solid #333;
        color: #000;
        background-color: #fafafa;
        font-size: 20px;
        font-weight: bold;
    }

    .menu-button:hover {
        background-color: #e0e0e0;
    }
</style>
</head>
<body>

<div class="page">

    <div class="list-area">
        <div class="list-title">ワークショップ一覧</div>

        <table>
            <tr>
                <th>ID</th>
                <th>タイトル</th>
                <th>開催日</th>
                <th>定員</th>
                <th>参加費</th>
            </tr>
            <tr>
                <td>1</td>
                <td>腕立て伏せ茶碗作り</td>
                <td>2026-06-10</td>
                <td>10</td>
                <td>3000円</td>
            </tr>
            <tr>
                <td>2</td>
                <td>マグカップ作り</td>
                <td>2026-06-12</td>
                <td>8</td>
                <td>2500円</td>
            </tr>
        </table>
    </div>

    <div class="button-area">
        <a class="menu-button" href="workshopAdd.jsp">ワークショップ追加</a>
        <a class="menu-button" href="workshopDeleteConfirm.jsp">ワークショップ削除</a>
        <a class="menu-button" href="workshopEdit.jsp">ワークショップ詳細編集</a>
    </div>

</div>

</body>
</html>