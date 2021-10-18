package model;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import database.DatabaseHelper;

public class SqliteDB {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "sqlite_dbname";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;

    private static SqliteDB sqliteDB;

    private SQLiteDatabase db;

    private SqliteDB(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取SqliteDB实例
     * @param context
     */
    public synchronized static SqliteDB getInstance(Context context) {
        if (sqliteDB == null) {
            sqliteDB = new SqliteDB(context);
        }
        return sqliteDB;
    }

    /**
     * 将User实例存储到数据库。
     */
    public int saveUser(User user) {
        if (user != null) {

            Log.d("activity","pass"+user.getUserpwd().toString());

            if (user.getUserpwd().length() == 0) {
                return 0;
            }

            Cursor cursor = db.rawQuery("select * from User where username=?", new String[]{user.getUsername().toString()});
            if (cursor.getCount() > 0) {
                return -1;
            }
            else {
                try {
                    db.execSQL("insert into User(username,userpwd, collection, trace) values(?,?,?,?) ", new String[]{user.getUsername(), user.getUserpwd(), user.getCollection(), user.getTrace()});
                }
                catch (Exception e) {
                    Log.d("Error", e.getMessage().toString());
                }
                return 1;
            }
        }
        else {
            return 0;
        }
    }

    /**
     * 从数据库读取User信息。
     */

    public int Quer(String pwd,String name)
    {


        HashMap<String,String> hashmap=new HashMap<String,String>();
        Cursor cursor =db.rawQuery("select * from User where username=?", new String[]{name});

        // hashmap.put("name",db.rawQuery("select * from User where name=?",new String[]{name}).toString());
        if (cursor.getCount()>0)
        {
            Cursor pwdcursor =db.rawQuery("select * from User where userpwd=? and username=?",new String[]{pwd,name});
            if (pwdcursor.getCount()>0)
            {
                return 1;
            }
            else {
                return -1;
            }
        }
        else {
            return 0;
        }
    }


    /**
     * 修改密码
     */
    public void Edit(String username, String newPwd)
    {
        String sql = "update User set userpwd=? where username=?";
        Object bindArgs[] = new Object[] { newPwd, username };
        db.execSQL(sql, bindArgs);
    }

    /**
     * 添加收藏
     */
    public void Collect(String name, String placeName)
    {
        Cursor cursor =db.rawQuery("select * from User where username=?", new String[]{name});
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex("collection");
        Log.d("XXXXXX", String.valueOf(nameColumnIndex));
        String newName = cursor.getString(nameColumnIndex) + placeName + ',';
        Log.d("TAG", newName);
        String sql = "update User set collection=? where username=?";
        Object bindArgs[] = new Object[] { newName, name };
        db.execSQL(sql, bindArgs);
        cursor.close();
    }

    /**
     * 读取收藏
     */
    public ArrayList<String> QuerCol (String userName)
    {
        String[] data = new String[]{};
        Cursor cursor =db.rawQuery("select * from User where username=?", new String[]{userName});
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex("collection");
        String all = cursor.getString(nameColumnIndex);
        data = all.split("\\,");
        Set set = new HashSet();
        for (int i = 0; i < data.length; i++) {
            set.add(data[i]);
        }
        data = (String[]) set.toArray(new String[0]);
        ArrayList<String> list = new ArrayList<String> (Arrays.asList(data));
        return list;
    }

    /**
     * 编辑行程
     */
    public void EditTrace(String name, String newTrace)
    {
        Cursor cursor =db.rawQuery("select * from User where username=?", new String[]{name});
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex("trace");
        Log.d("XXXXXX", String.valueOf(nameColumnIndex));
        String sql = "update User set trace=? where username=?";
        Object bindArgs[] = new Object[] { newTrace, name };
        db.execSQL(sql, bindArgs);
        cursor.close();

    }

    /**
     * 读取行程
     */
    public String[] QuerTrace (String userName)
    {
        String[] data = new String[]{};
        Cursor cursor =db.rawQuery("select * from User where username=?", new String[]{userName});
        cursor.moveToFirst();
        int nameColumnIndex = cursor.getColumnIndex("trace");
        String all = cursor.getString(nameColumnIndex);
        data = all.split("\\,");
        Set set = new HashSet();
        for (int i = 0; i < data.length; i++) {
            set.add(data[i]);
        }
        data = (String[]) set.toArray(new String[0]);
        return data;
    }

}
