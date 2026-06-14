-- ============================================================
-- pottery データベース 初期化スクリプト
--   DTO / DAO から逆算した、修正版アプリ動作確認用スキーマ＋サンプルデータ
--   接続情報: jdbc:postgresql://localhost:5432/pottery (postgres / password)
-- ============================================================

DROP TABLE IF EXISTS tbl_book CASCADE;
DROP TABLE IF EXISTS tbl_workshop CASCADE;
DROP TABLE IF EXISTS tbl_course CASCADE;
DROP TABLE IF EXISTS tbl_user CASCADE;
DROP TABLE IF EXISTS tbl_admin CASCADE;
DROP TABLE IF EXISTS tbl_info CASCADE;

-- ユーザー（UserDao 参照）
CREATE TABLE tbl_user (
    user_id      BIGSERIAL PRIMARY KEY,
    user_name    VARCHAR(100),
    email        VARCHAR(255) UNIQUE,
    password     VARCHAR(255),
    phone_number VARCHAR(50),
    address      VARCHAR(255)
);

-- 管理者（AdminDao 参照）
CREATE TABLE tbl_admin (
    admin_id       BIGSERIAL PRIMARY KEY,
    admin_name     VARCHAR(100),
    admin_email    VARCHAR(255),
    admin_password VARCHAR(255)
);

-- お知らせ（InfoDao 参照）
CREATE TABLE tbl_info (
    info_id   BIGSERIAL PRIMARY KEY,
    title     VARCHAR(255),
    content   TEXT,
    post_date DATE
);

-- コース（CourseDao / WorkShopDao 参照）
CREATE TABLE tbl_course (
    course_id     BIGSERIAL PRIMARY KEY,
    course_name   VARCHAR(255),
    price         INT,
    required_time INT,
    description   TEXT
);

-- ワークショップ開催枠（WorkShopDao 参照、capacity = 残り枠数）
CREATE TABLE tbl_workshop (
    workshop_id   BIGSERIAL PRIMARY KEY,
    course_id     BIGINT REFERENCES tbl_course(course_id),
    workshop_date DATE,
    start_time    TIME,
    capacity      INT
);

-- 予約（ReservationDao 参照）
CREATE TABLE tbl_book (
    book_id     BIGSERIAL PRIMARY KEY,
    workshop_id BIGINT REFERENCES tbl_workshop(workshop_id),
    user_id     BIGINT REFERENCES tbl_user(user_id),
    guest_name  VARCHAR(255),
    num_people  INT
);

-- ------------------------------------------------------------
-- サンプルデータ
-- ------------------------------------------------------------

-- ログイン確認用ユーザー（email / password は平文比較）
INSERT INTO tbl_user (user_name, email, password, phone_number, address) VALUES
    ('テスト太郎', 'user@test.com', 'password', '090-1111-2222', '福岡県福岡市中央区1-1-1');

-- 管理者
INSERT INTO tbl_admin (admin_name, admin_email, admin_password) VALUES
    ('管理者', 'admin@test.com', 'password');

-- お知らせ
INSERT INTO tbl_info (title, content, post_date) VALUES
    ('開講のお知らせ', '陶筋工房のワークショップ予約を開始しました。', DATE '2026-06-01');

-- コース
INSERT INTO tbl_course (course_name, price, required_time, description) VALUES
    ('電動ろくろ体験', 4500, 90, '電動ろくろでお茶碗やカップを作る基本コースです。'),
    ('手びねり体験',   3500, 60, '手びねりでお皿やマグカップを自由に作るコースです。'),
    ('絵付け体験',     2800, 45, '素焼きの器に好きな絵を描く絵付け専門コースです。');

-- ワークショップ開催枠（今日 2026-06-15 以降の未来日程・残枠あり）
INSERT INTO tbl_workshop (course_id, workshop_date, start_time, capacity) VALUES
    (1, DATE '2026-06-20', TIME '10:00', 5),
    (1, DATE '2026-06-20', TIME '14:00', 3),
    (2, DATE '2026-06-21', TIME '10:00', 6),
    (2, DATE '2026-06-28', TIME '13:00', 2),
    (3, DATE '2026-07-04', TIME '11:00', 8),
    (1, DATE '2026-07-12', TIME '10:00', 1);
