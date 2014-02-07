package com.example.loaderexample;

import android.content.Context;
import android.database.ContentObserver;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

public class UsersLoader extends AsyncTaskLoader<List<User>> {

  private UserDatabase userDatabase;

  public UsersLoader(Context context) {
    super(context);
    userDatabase = UserDatabase.get(context);


    /**
     * callBack with onChange() method
     * which will be called when DB change detected
     * ( in DB will be called "getContentResolver().notifyChange()" )
     * and this observer will call forceLoad();
     */
    ContentObserver observer = new ForceLoadContentObserver();

    /**
     * register observer which keep track of changes connected
     * with specified database table specified in URI
     */
    getContext().getContentResolver().registerContentObserver(UserDatabase.USER_URI, true, observer);
  }

  @Override
  public List<User> loadInBackground() {
    return userDatabase.getAllUsers();
  }

  @Override
  protected void onStartLoading() {
    forceLoad();
  }
}
