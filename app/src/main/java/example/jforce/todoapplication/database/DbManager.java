package example.jforce.todoapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import example.jforce.todoapplication.helper.SharedPref;
import example.jforce.todoapplication.model.taskHelper;

/**
 * Created by harshal on 5/18/2019.
 */

public class DbManager {
    private Context ctx;
    private Database db;

    public DbManager(Context ctx) {
        this.ctx = ctx;
        this.db = new Database(ctx, "TODODB", null, 1);
    }

    // user methods
    public long insertUser(String Name, String Contact, String Email, String Password) {
        long result = -1;
        ContentValues cv = new ContentValues();
        cv.put(Database.USER_NAME, Name);
        cv.put(Database.USER_CONTACT, Contact);
        cv.put(Database.USER_EMAIL, Email);
        cv.put(Database.USER_PASSWORD, Password);
        SQLiteDatabase my_db = db.getWritableDatabase();
        Cursor cursor = my_db.query(Database.TABLE_USERS, null, Database.USER_EMAIL + " =?", new String[]{Email}, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.close();
            my_db.close();
            result = -2;
        } else {
            result = my_db.insert(Database.TABLE_USERS, null, cv);
            cursor.close();
            my_db.close();
        }
        return result;
    }

    // profile update with password
    public boolean updateUser(String Name, String Contact, String Password, String uid) {
        long result = -1;
        ContentValues cv = new ContentValues();
        cv.put(Database.USER_NAME, Name);
        cv.put(Database.USER_CONTACT, Contact);
        cv.put(Database.USER_PASSWORD, Password);
        SQLiteDatabase my_db = db.getWritableDatabase();
        result = my_db.update(Database.TABLE_USERS, cv, Database.USER_ID + " =? ", new String[]{uid});
        my_db.close();
        if (result > 0) {
            new SharedPref(ctx).SaveDataUpdate(Name, Contact, Password);
            return true;
        }
        return false;
    }

    // profile update with out password
    public boolean updateUser(String Name, String Contact, String uid) {
        long result = -1;
        ContentValues cv = new ContentValues();
        cv.put(Database.USER_NAME, Name);
        cv.put(Database.USER_CONTACT, Contact);
        SQLiteDatabase my_db = db.getWritableDatabase();
        result = my_db.update(Database.TABLE_USERS, cv, Database.USER_ID + " =? ", new String[]{uid});
        my_db.close();
        if (result > 0) {
            new SharedPref(ctx).SaveDataUpdate(Name, Contact);
            return true;
        }
        return false;
    }

    public boolean LoginUser(String Email, String Password) {

        SQLiteDatabase my_db = db.getWritableDatabase();
        Cursor result = my_db.query(Database.TABLE_USERS, null, Database.USER_EMAIL + " =?  and " + Database.USER_PASSWORD + " =? ", new String[]{Email, Password}, null, null, null);

        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            new SharedPref(ctx).SaveData(result.getString(0), result.getString(1), result.getString(3), result.getString(2), Password);
            my_db.close();
            result.close();
            return true;
        } else {
            my_db.close();
            result.close();
            return false;
        }

    }


    // task methods
    public boolean insertTask(String date, String task) {
        long result = -1;
        ContentValues cv = new ContentValues();
        cv.put(Database.DATE, date);
        cv.put(Database.TASK, task);
        cv.put(Database.STATUS, "0");
        cv.put(Database.TASK_USER_ID, new SharedPref(ctx).getUserId());
        SQLiteDatabase my_db = db.getWritableDatabase();
        result = my_db.insert(Database.TABLE_TASK, null, cv);
        my_db.close();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public boolean UpdateTask(String id) {
        long result = -1;
        ContentValues cv = new ContentValues();
        cv.put(Database.STATUS, "1");
        SQLiteDatabase my_db = db.getWritableDatabase();
        result = my_db.update(Database.TABLE_TASK, cv, Database.ID + " =? ", new String[]{id});
        my_db.close();
        if (result > 0) {
            return true;
        }
        return false;
    }

    public ArrayList<taskHelper> ListOfTask(String date, String status) {
        ArrayList<taskHelper> result_data = new ArrayList<taskHelper>();
        SQLiteDatabase my_db = db.getWritableDatabase();
        Cursor result = my_db.query(Database.TABLE_TASK, null, Database.DATE + " =? and " + Database.STATUS + "=? and " + Database.TASK_USER_ID + " =?", new String[]{date, status, new SharedPref(ctx).getUserId()}, null, null, null);

        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            do {
                taskHelper th = new taskHelper(result.getString(result.getColumnIndex(db.ID)), result.getString(result.getColumnIndex(db.TASK)), result.getString(result.getColumnIndex(db.TASK_USER_ID)), result.getString(result.getColumnIndex(db.DATE)), result.getString(result.getColumnIndex(db.STATUS)));

                result_data.add(th);
            } while (result.moveToNext());

            my_db.close();
            result.close();
        }
        return result_data;
    }

    public ArrayList<taskHelper> ListOfAllTask() {
        ArrayList<taskHelper> result_data = new ArrayList<taskHelper>();
        SQLiteDatabase my_db = db.getWritableDatabase();
        Cursor result = my_db.query(Database.TABLE_TASK, null, Database.TASK_USER_ID + " =?", new String[]{new SharedPref(ctx).getUserId()}, null, null, Database.DATE + " DESC");

        if (result != null && result.getCount() > 0) {
            result.moveToFirst();
            do {
                taskHelper th = new taskHelper(result.getString(0), result.getString(2), result.getString(1), result.getString(3), result.getString(4));
                result_data.add(th);
            } while (result.moveToNext());

            my_db.close();
            result.close();
        }
        return result_data;
    }
}

