<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>管理者top</title>
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
            box-sizing: border-box;
            padding: 10px 20px 40px 20px;
        }

        /* 管理者top は左上のまま */
        .page-title {
            font-size: 20px;
            font-weight: bold;
            color: #333333;
            margin-bottom: 25px;
        }

        /* 管理者top以外を中央寄せ */
        .menu-wrapper {
            width: 100%;
            display: flex;
            flex-direction: column;
            align-items: center;
        }

        /* すべてのボタンサイズを統一 */
        .menu-button {
            width: 420px;
            height: 90px;
            border: 2px solid #333333;
            background-color: #eeeeee;
            font-size: 22px;
            font-weight: bold;
            cursor: pointer;
            box-sizing: border-box;
        }

        .menu-button:hover {
            background-color: #dddddd;
        }

        .top-form {
            margin: 0 0 40px 0;
        }

        .menu-grid {
            display: grid;
            grid-template-columns: 420px 420px;
            column-gap: 40px;
            row-gap: 35px;
            justify-content: center;
        }

        .menu-form {
            margin: 0;
        }
    </style>
</head>
<body>

<div class="page">
    <div class="page-title">管理者top</div>

    <div class="menu-wrapper">
        <!-- 予約一覧 -->
        <form action="reservationList.jsp" method="get" class="top-form">
            <button type="submit" class="menu-button">予約一覧</button>
        </form>

        <!-- 下の4つ -->
        <div class="menu-grid">
            <form action="noticeList.jsp" method="get" class="menu-form">
                <button type="submit" class="menu-button">お知らせ一覧</button>
            </form>

            <form action="workshopList.jsp" method="get" class="menu-form">
                <button type="submit" class="menu-button">ワークショップ開催枠設定</button>
            </form>

            <form action="memberList.jsp" method="get" class="menu-form">
                <button type="submit" class="menu-button">会員一覧</button>
            </form>

            <form action="courseList.jsp" method="get" class="menu-form">
                <button type="submit" class="menu-button">コース一覧</button>
            </form>
        </div>
    </div>
</div>

</body>
</html>
