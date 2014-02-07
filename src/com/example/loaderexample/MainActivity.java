package com.example.loaderexample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class MainActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<List<User>> {

  public final static int SHOW_USERS_LOADER_ID = 1;

  private ArrayAdapter<User> adapter;
  private UserDatabase userDatabase;
  private Random random;
  private Context context;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    context = this;
    userDatabase = UserDatabase.get(context);
    adapter = new ArrayAdapter<User>(context, android.R.layout.simple_list_item_1);
    random = new Random();

    // should be called in onCreate()
    getSupportLoaderManager().initLoader(SHOW_USERS_LOADER_ID, null, this);

    // we call it before UI initialization, but loader will start update UI
    // only after onCreate() finished, so it's ok
    getSupportLoaderManager().getLoader(SHOW_USERS_LOADER_ID).startLoading();

    initUi();
  }

  private void initUi() {
    ListView usersView = (ListView) findViewById(R.id.users);
    usersView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        User user = adapter.getItem(position);
        userDatabase.removeUser(adapter.getItem(position));
        Toast.makeText(getApplicationContext(), "User#id" + user.getId() + " removed", Toast.LENGTH_SHORT).show();
      }
    });
    usersView.setAdapter(adapter);

    findViewById(R.id.makeDb).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        userDatabase.addUser(new User("ManualUser#" + random.nextInt(1000), "email"));
      }
    });

    findViewById(R.id.clear).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        userDatabase.removeAllUsers();
      }
    });

    findViewById(R.id.startService).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        startService(new Intent(context, DbService.class));
      }
    });
  }

  @Override
  public Loader<List<User>> onCreateLoader(int id, Bundle args) {
    return new UsersLoader(getApplicationContext());
  }

  @Override
  public void onLoadFinished(Loader<List<User>> loader, List<User> data) {
    if (loader.getId() == SHOW_USERS_LOADER_ID) {
      adapter.clear();
      // adapter.addAll() - available only from api 11
      if (data != null) {
        for (int i = 0; i < data.size(); i++) {
          adapter.add(data.get(i));
        }
      }
      adapter.notifyDataSetChanged();
    }
  }

  @Override
  public void onLoaderReset(Loader<List<User>> loader) {
  }

}
