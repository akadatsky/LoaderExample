package com.example.loaderexample;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import com.mobindustry.cursormapper.CursorMapper;

import java.util.List;

public class UserDatabase extends SQLiteOpenHelper {

  public final static String PACKAGE = "com.example.loaderexample";

  // standard format for URI: "content://<package>.resolver/<table>"
  public final static Uri USER_URI = Uri.parse("content://" + PACKAGE + ".resolver/user");

  private static final String USER_TABLE = "user";
  private static final CursorMapper<User> userMapper = CursorMapper.create(User.class);

  static final String ID = "_id";
  static final String NAME = "name";
  static final String EMAIL = "email";

  static final String DB_CREATE = "create table " + USER_TABLE + "("
      + ID + " integer primary key autoincrement, "
      + NAME + " text, " + EMAIL + " text" + ");";

  private static final int CURRENT_VERSION = 1;
  private static final String DATABASE_NAME = "user.db";

  private static UserDatabase singleton;

  private Context context;

  private UserDatabase(Context context) {
    super(context, DATABASE_NAME, null, CURRENT_VERSION);
    this.context = context;
  }

  public synchronized static UserDatabase get(Context context) {
    if (singleton == null) {
      singleton = new UserDatabase(context.getApplicationContext());
    }
    return singleton;
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    onUpgrade(sqLiteDatabase, 0, CURRENT_VERSION);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int currentVersion, int newVersion) {
    for (int version = currentVersion + 1; version <= newVersion; version++) {
      if (version == 1) {
        sqLiteDatabase.execSQL(DB_CREATE);
      }
    }
  }

  public Context getContext() {
    return context;
  }

  public long addUser(User user) {
    ContentValues values = new ContentValues();
    values.put(NAME, user.getName());
    values.put(EMAIL, user.getEmail());
    long id = getWritableDatabase().insert(USER_TABLE, null, values);
    getContext().getContentResolver().notifyChange(USER_URI, null);
    return id;
  }

  public List<User> getAllUsers() {
    Cursor cursor = getReadableDatabase().query(USER_TABLE, null, null, null, null, null, "_id DESC");
    return userMapper.map(cursor);
  }

  public void removeUser(User user) {
    getWritableDatabase().delete(
        USER_TABLE,
        "_id = ?",
        new String[]{String.valueOf(user.getId())});
    getContext().getContentResolver().notifyChange(USER_URI, null);
  }

  public void removeAllUsers() {
    getWritableDatabase().delete(USER_TABLE, null, null);
    getContext().getContentResolver().notifyChange(USER_URI, null);
  }

}
