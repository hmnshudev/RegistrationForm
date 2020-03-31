package com.example.registrationform;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_NAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_TIME = "time";
    private static final String KEY_MOBILE = "moblie";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_IMAGE = "image";
    private static final String SQL_TABLE_USERS = " CREATE TABLE " + TABLE_USERS
            + " ( "
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_USER_NAME + " TEXT, "
            + KEY_EMAIL + " TEXT, "
            + KEY_PASSWORD + " TEXT,"
            + KEY_TIME + " TEXT,"
            + KEY_MOBILE + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_GENDER + " TEXT,"
            + KEY_LANGUAGE + " TEXT,"
            + KEY_IMAGE + " BLOB"
            + " ) ";

    SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(" DROP TABLE IF EXISTS " + TABLE_USERS);
    }

    void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, user.userName);
        values.put(KEY_EMAIL, user.email);
        values.put(KEY_PASSWORD, user.password);
        values.put(KEY_TIME, user.time);
        values.put(KEY_MOBILE, user.mobile);
        values.put(KEY_ADDRESS, user.address);
        values.put(KEY_GENDER, user.gender);
        values.put(KEY_LANGUAGE, user.language);
        values.put(KEY_IMAGE, user.image);
        db.insert(TABLE_USERS, null, values);
    }

    void deleteAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_USERS, null, null);
        db.execSQL("delete from " + TABLE_USERS);
        db.close();
    }

    User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_TIME, KEY_MOBILE, KEY_ADDRESS, KEY_GENDER, KEY_LANGUAGE, KEY_IMAGE},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getBlob(9));
            if (user.password.equals(user1.password)) {
                cursor.close();
                return user1;
            }
        }
        return null;
    }

    ArrayList<User> getAllUserInfo() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("Select *from " + TABLE_USERS, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            do {
                User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8), cursor.getBlob(9));
                userList.add(user1);
            } while (cursor.moveToNext());
            cursor.close();
            return userList;
        } else {
            return userList;
        }
    }


    boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS,// Selecting Table
                new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD, KEY_TIME, KEY_MOBILE, KEY_ADDRESS, KEY_GENDER, KEY_LANGUAGE, KEY_IMAGE},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);


        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            cursor.close();
            return true;
        }

        return false;
    }
}





