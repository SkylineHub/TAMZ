package com.example.coinapocalypse;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "CAScore.db";
    public static final String ITEM_COLUMN_NAME = "name";
    public static final String ITEM_COLUMN_SCORE = "score";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE items " + "(id INTEGER PRIMARY KEY, name TEXT, score INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS items");
        onCreate(db);
    }

    public boolean insertItem(String name, Integer score)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("score", score);
        long insertedId = db.insert("items", null, contentValues);
        if (insertedId == -1) return false;
        return true;
    }

    public boolean deleteItem (int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long insertedId = db.delete("items", "id=?", new String[] {Integer.toString(id)});
        if (insertedId == -1) return false;
        return true;
    }

    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from items where id=" + id + "", null);
        return res;
    }

    public boolean updateItem (Integer id, String name, Integer score)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        contentValues.put("score", score);
        long insertedId = db.update("items", contentValues, "id=?", new String[] {Integer.toString(id)});
        if (insertedId == -1) return false;
        return true;
    }

    public ArrayList<String> getItemList()
    {
        ArrayList<String> arrayList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from items order by score desc", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            String name = res.getString(res.getColumnIndex(ITEM_COLUMN_NAME));
            String score = res.getString(res.getColumnIndex(ITEM_COLUMN_SCORE));
            int id = res.getInt(0);
            arrayList.add("Attempt: " + id + "     Nick: " + name + "     Score: " + score);
            res.moveToNext();
        }

        return arrayList;
    }

    public int removeAll()
    {
        int nRecordDeleted = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res =  db.rawQuery( "select id from items", null);
        nRecordDeleted = res.getCount();
        long insertedId = db.delete("items", null, null);
        if (insertedId == -1) return nRecordDeleted;
        return nRecordDeleted;
    }
}
