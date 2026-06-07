-- =====================================================================
--  陶芸体験ワークショップ Web予約システム  データベース設計 (PostgreSQL)
--  DBMS: PostgreSQL 14+
--
--  【実行手順】
--   1) データベースを作成する(psql から):
--        CREATE DATABASE pottery_reservation ENCODING 'UTF8';
--   2) 作成したDBに接続して本スクリプトを実行する:
--        psql -U postgres -d pottery_reservation -f database/schema.sql
--      もしくは:  \c pottery_reservation  の後に \i database/schema.sql
-- =====================================================================

-- 再実行できるよう既存テーブルを削除(依存順に CASCADE)
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS courses      CASCADE;
DROP TABLE IF EXISTS workshops    CASCADE;
DROP TABLE IF EXISTS notices      CASCADE;
DROP TABLE IF EXISTS members      CASCADE;

-- ---------------------------------------------------------------------
--  会員テーブル (members)
--   会員登録情報: 氏名 / メールアドレス / 電話番号 / 住所 / パスワード
--   role: 'USER'(一般会員) / 'ADMIN'(管理者)
-- ---------------------------------------------------------------------
CREATE TABLE members (
    member_id  INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name       VARCHAR(100)  NOT NULL,
    email      VARCHAR(255)  NOT NULL UNIQUE,
    phone      VARCHAR(20)   NOT NULL,
    address    VARCHAR(255)  NOT NULL,
    password   VARCHAR(255)  NOT NULL,                 -- 平文で保存(演習用。実運用ではハッシュ化必須)
    role       VARCHAR(10)   NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_members_role CHECK (role IN ('USER', 'ADMIN'))
);

-- ---------------------------------------------------------------------
--  ワークショップテーブル (workshops)
--   開催日(event_date)を持ち、カレンダー表示に利用する
-- ---------------------------------------------------------------------
CREATE TABLE workshops (
    workshop_id INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title       VARCHAR(150)  NOT NULL,
    description TEXT          NOT NULL,
    instructor  VARCHAR(100)  NOT NULL,
    event_date  DATE          NOT NULL,                -- 開催日
    start_time  TIME          NOT NULL,                -- 開始時刻
    capacity    INTEGER       NOT NULL DEFAULT 10,     -- 定員
    price       INTEGER       NOT NULL DEFAULT 0,      -- 参加費(円)
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_workshops_event_date ON workshops (event_date);

-- ---------------------------------------------------------------------
--  コーステーブル (courses)
--   ワークショップ予約時に選択する体験コース(プラン)のマスタ。
--   どのワークショップでも共通で選べる(全体マスタ)。
-- ---------------------------------------------------------------------
CREATE TABLE courses (
    course_id   INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name        VARCHAR(100)  NOT NULL,                -- コース名
    description TEXT          NOT NULL,                -- コース説明
    price       INTEGER       NOT NULL DEFAULT 0,      -- コース追加料金(円)
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ---------------------------------------------------------------------
--  予約テーブル (reservations)
--   会員 × ワークショップ の予約。予約時に選択したコースも保持する。
--   同一会員が同一WSを重複予約できないよう部分一意インデックスで制約
--   (キャンセル済みは除外し、再予約を許容する)
-- ---------------------------------------------------------------------
CREATE TABLE reservations (
    reservation_id   INTEGER     GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    member_id        INTEGER     NOT NULL REFERENCES members(member_id)   ON DELETE CASCADE,
    workshop_id      INTEGER     NOT NULL REFERENCES workshops(workshop_id) ON DELETE CASCADE,
    course_id        INTEGER     NOT NULL REFERENCES courses(course_id),   -- 選択コース
    number_of_people INTEGER     NOT NULL DEFAULT 1,
    status           VARCHAR(15) NOT NULL DEFAULT 'CONFIRMED',   -- CONFIRMED / CANCELED
    reserved_at      TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_res_status CHECK (status IN ('CONFIRMED', 'CANCELED'))
);
-- 「予約確定(CONFIRMED)」の状態でのみ会員×WSを一意にする部分インデックス
CREATE UNIQUE INDEX uq_active_reservation
    ON reservations (member_id, workshop_id)
    WHERE status = 'CONFIRMED';

-- ---------------------------------------------------------------------
--  お知らせテーブル (notices)
--   管理者からのお知らせ
-- ---------------------------------------------------------------------
CREATE TABLE notices (
    notice_id  INTEGER       GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title      VARCHAR(150)  NOT NULL,
    content    TEXT          NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- PostgreSQL には MySQL の "ON UPDATE CURRENT_TIMESTAMP" が無いため
-- 更新時に updated_at を自動更新するトリガを定義する
CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_notices_updated_at
    BEFORE UPDATE ON notices
    FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =====================================================================
--  初期データ
-- =====================================================================

-- 管理者アカウント (email: admin@pottery.com / password: admin123)
-- ※ password は平文で保存(演習用)
INSERT INTO members (name, email, phone, address, password, role) VALUES
('システム管理者', 'admin@pottery.com', '00-0000-0000', '東京都',
 'admin123', 'ADMIN');

-- サンプル一般会員 (email: taro@example.com / password: user123)
INSERT INTO members (name, email, phone, address, password, role) VALUES
('山田 太郎', 'taro@example.com', '090-1234-5678', '東京都新宿区1-1-1',
 'user123', 'USER');

-- サンプルワークショップ
INSERT INTO workshops (title, description, instructor, event_date, start_time, capacity, price) VALUES
('はじめての手びねり体験', '土の感触を楽しみながら、世界に一つだけの器を作ります。初心者大歓迎です。', '佐藤 花子', '2026-06-15', '10:00', 8, 3500),
('電動ろくろ陶芸教室',     '電動ろくろを使って本格的な器づくりに挑戦。経験者向けのコースです。',           '鈴木 一郎', '2026-06-20', '14:00', 6, 5000),
('親子で楽しむ陶芸ワークショップ', '小さなお子様と一緒に楽しめる陶芸体験。家族の思い出を形にしましょう。',   '佐藤 花子', '2026-07-05', '11:00', 10, 4000);

-- サンプルコース(予約時に選択)
INSERT INTO courses (name, description, price) VALUES
('スタンダードコース', '基本の作陶体験。器を1点制作します。',                 0),
('じっくり体験コース', '時間をかけて2点制作。仕上げの装飾までこだわれます。', 1500),
('ペア・親子コース',   '2名1組での参加向け。お揃いの器づくりが楽しめます。',   1000);

-- サンプルお知らせ
INSERT INTO notices (title, content) VALUES
('7月の開講スケジュールについて', '7月のワークショップ日程を公開しました。人気のコースは早めにご予約ください。'),
('夏季休業のお知らせ',           '8月13日〜8月16日は休業とさせていただきます。ご予約はお早めにお願いいたします。');
