package com.example.loaderexample;

import android.app.IntentService;
import android.content.Intent;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class DbService extends IntentService {

  private UserDatabase userDatabase;
  private Random random;

  public DbService() {
    super("DbService");
  }

  @Override
  public void onCreate() {
    super.onCreate();
    userDatabase = UserDatabase.get(getApplicationContext());
    random = new Random();
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    for (int i = 0; i < 5; i++) {
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      userDatabase.addUser(new User("ServiceUser#" + random.nextInt(1000), "email"));
    }
  }

}
