<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>予約管理メニュー</title>
    <style>
        body {
            margin: 0;
            padding: 0;
            background-color: #dcdcdc;
            font-family: "Yu Gothic", "Hiragino Kaku Gothic ProN", sans-serif;
        }

        .container {
            width: 90%;
            max-width: 1000px;
            margin: 30px auto;
            padding: 40px 30px;
            border: 2px solid #333;
            background-color: #dcdcdc;
            box-sizing: border-box;
        }

        .menu-box {
            width: 90%;
            height: 320px;
            margin: 0 auto 30px auto;
            border: 2px solid #333;
            background-color: #eeeeee;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 28px;
            font-weight: bold;
            color: #333;
        }

        .button-area {
            width: 90%;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            gap: 20px;
        }

        .button-form {
            width: 32%;
            margin: 0;
        }

        .action-button {
            width: 100%;
            height: 70px;
            border: 2px solid #333;
            background-color: #eeeeee;
            font-size: 22px;
            font-weight: bold;
            cursor: pointer;
        }

        .action-button:hover {
            background-color: #dddddd;
        }
    </style>
</head>
<body>

    <div class="container">
        <div class="menu-box">
            予約管理メニュー
        </div>

        <div class="button-area">
            reservationList.jsp
                <button type="submit" class="action-button">予約一覧</button>
            </form>

            cancelReservation.jsp
                <button type="submit" class="action-button">予約消去</button>
            </form>

            scheduleEditing.jsp
                <button type="submit" class="action-button">予約編集</button>
            </form>
        </div>
    </div>

</body>
</html>