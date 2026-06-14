<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ワークショップ追加</title>

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

    .form-wrapper {
        width: 320px;
        margin: 40px auto 0 auto;
        display: flex;
        flex-direction: column;
        gap: 20px;
    }

    .form-group {
        display: flex;
        flex-direction: column;
        gap: 8px;
    }

    .form-label {
        font-size: 18px;
        font-weight: bold;
        text-align: center;
    }

    .form-input {
        width: 100%;
        height: 60px;
        border: 2px solid #3a5874;
        font-size: 22px;
        text-align: center;
        box-sizing: border-box;
        background-color: #fafafa;
    }

    .save-area {
        width: 320px;
        margin: 80px auto 0 auto;
        text-align: center;
    }

    .save-button {
        width: 100%;
        height: 70px;
        border: 2px solid #3a5874;
        background-color: #fafafa;
        font-size: 32px;
        cursor: pointer;
    }

    .save-button:hover {
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

    .page-title {
        text-align: center;
        font-size: 34px;
        font-weight: bold;
        margin-bottom: 30px;
    }
</style>
</head>
<body>

<div class="page">

    <div class="page-title">ワークショップ追加</div>

    workshopAddConfirm

        <div class="form-wrapper">

            <div class="form-group">
                <label class="form-label" for="title">コース名</label>
                <input class="form-input" type="text" id="title" name="title" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="capacity">定員</label>
                <input class="form-input" type="number" id="capacity" name="capacity" min="1" required>
            </div>

            <div class="form-group">
                <label class="form-label" for="workshopDateTime">開催日時</label>
                <input class="form-input" type="datetime-local" id="workshopDateTime" name="workshopDateTime" required>
            </div>

        </div>

        <div class="save-area">
            <button type="submit" class="save-button">保存</button>
        </div>

    </form>

    <div class="back-area">
        workshopList戻る</a>
    </div>

</div>

</body>
</html>