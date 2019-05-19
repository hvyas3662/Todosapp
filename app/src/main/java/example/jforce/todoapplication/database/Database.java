package example.jforce.todoapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *Created by harshal on 5/18/2019.
 */

public class Database extends SQLiteOpenHelper {



    // Table Names
    public static final String TABLE_TASK = "task";
    public static final String TABLE_USERS = "users";

    // task Table Columns
    public static final String ID = "id";
    public static final String TASK_USER_ID = "userId";
    public static final String TASK = "task";
    public static final String DATE = "data";
    public static final String STATUS = "status";

    // User Table Columns
    public static final String USER_ID = "id";
    public static final String USER_NAME = "userName";
    public static final String USER_EMAIL = "userEmail";
    public static final String USER_CONTACT = "userContact";
    public static final String USER_PASSWORD = "userPassword";

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASK +
                "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TASK_USER_ID + " INTEGER," +
                TASK + " TEXT," +
                DATE + " TEXT," +
                STATUS + " TEXT" +
                ")";

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS +
                "(" +
                USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USER_NAME + " TEXT," +
                USER_EMAIL + " TEXT," +
                USER_CONTACT + " TEXT," +
                USER_PASSWORD + " TEXT" +
                ")";

        db.execSQL(CREATE_TASK_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            // Simplest implementation is to drop all old tables and recreate them
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }



}
