package com.pottery.reservation.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * データベース接続を一元管理するユーティリティクラス。
 *
 * 【動作の解説】
 * - クラスが初めて使われたとき static イニシャライザで PostgreSQL の
 *   JDBCドライバ(org.postgresql.Driver)を一度だけロードする。
 * - getConnection() は呼ばれるたびに新しい接続を生成して返す。
 *   呼び出し側(DAO)が try-with-resources で close() するため、
 *   このクラス自身は接続を保持しない(シンプルな実装)。
 * - 接続情報は環境に合わせて URL / USER / PASSWORD を書き換えること。
 */
public class DBConnection {

    // PostgreSQL の接続URL。デフォルトポートは 5432。
    private static final String URL =
            "jdbc:postgresql://localhost:5432/pottery_reservation";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";   // 環境に合わせて変更

    static {
        try {
            // PostgreSQL JDBCドライバのロード(JDBC 4.0以降は省略可だが明示しておく)
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("JDBCドライバの読み込みに失敗しました: " + e.getMessage());
        }
    }

    private DBConnection() {
        // インスタンス化禁止
    }

    /**
     * データベースへの新しい接続を取得する。
     * @return Connection
     * @throws SQLException 接続失敗時
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
