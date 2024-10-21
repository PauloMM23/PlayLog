package com.example.playlog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GameDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "playlog.db";
    private static final int DATABASE_VERSION = 1;

    public GameDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE games (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, platform TEXT, status TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS games");
        onCreate(db);
    }

    // Método para adicionar um jogo ao banco de dados
    public boolean addGame(String title, String platform, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("platform", platform);
        values.put("status", status);

        long result = db.insert("games", null, values);
        return result != -1;
    }

    // Método para deletar um jogo pelo título
    public boolean deleteGame(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("games", "title=?", new String[]{title}) > 0;
    }

    // Método para atualizar um jogo
    public boolean updateGame(String oldTitle, String newPlatform, String newStatus) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("platform", newPlatform);
        values.put("status", newStatus);

        return db.update("games", values, "title=?", new String[]{oldTitle}) > 0;
    }

    // Método para listar os jogos
    public Cursor getAllGames() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM games", null);
    }
}

