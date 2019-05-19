package example.jforce.todoapplication.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by harshal on 5/18/2019.
 */

public class SharedPref {
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public SharedPref(Context ctx) {
        sp = ctx.getSharedPreferences("my_app_toods", 0);
        editor = sp.edit();
    }

    public void SaveData(String id, String name, String contact, String email, String password) {
        editor.putString("uid", id);
        editor.putString("name", name);
        editor.putString("contact", contact);
        editor.putString("email", email);
        editor.putString("pass", password);
        editor.commit();
    }

    public void SaveDataUpdate(String name, String contact, String password) {
        editor.putString("name", name);
        editor.putString("contact", contact);
        editor.putString("pass", password);
        editor.commit();
    }

    public void SaveDataUpdate(String name, String contact) {
        editor.putString("name", name);
        editor.putString("contact", contact);
        editor.commit();
    }

    public SharedPreferences getsp() {
        return sp;
    }

    public String getUserId() {
        return sp.getString("uid", "-1");
    }

    public String getUserName() {
        return sp.getString("name", "");
    }

    public String getUserContact() {
        return sp.getString("contact", "");
    }

    public String getUserEmail() {
        return sp.getString("email", "");
    }


    public void clearAll() {
        editor.clear();
        editor.commit();
    }
}
