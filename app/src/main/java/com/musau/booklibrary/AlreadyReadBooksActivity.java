package com.musau.booklibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class AlreadyReadBooksActivity extends AppCompatActivity implements BookRecyclerViewAdapter.OnDeletingBook {
    private static final String TAG = "AlreadyReadBooks";

    private BookRecyclerViewAdapter adapter;
    private ArrayList<Book> alreadyReadBooks;
    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_read_books);

        overridePendingTransition(R.anim.in,R.anim.out);

        BottomNavigationView bottomNavigationView = findViewById(R.id.alreadyReadBooksBottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.icon_readBooks);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.icon_home:
                        Intent intent = new Intent(AlreadyReadBooksActivity.this,MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.icon_library:
                        Intent readBooksIntent = new Intent(AlreadyReadBooksActivity.this,LibraryActivity.class);
                        readBooksIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(readBooksIntent);
                        break;
                    case R.id.icon_readBooks:
                        //home activity
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        alreadyReadBooks = new ArrayList<>();

        RecyclerView recyclerView = findViewById(R.id.alreadyReadBooksRecyclerView);
        adapter = new BookRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AlreadyReadBooksAsyncTask asyncTask = new AlreadyReadBooksAsyncTask();
        asyncTask.execute();
    }

    @Override
    public void onDeletingBookResult(Book book) {
        Log.d(TAG, "onDeletingBookResult: deleting " + book.toString());
        helper.deleteBookFromLAlreadyReadBooks(database,book);
        AlreadyReadBooksAsyncTask asyncTask = new AlreadyReadBooksAsyncTask();
        asyncTask.execute();
    }

    private class AlreadyReadBooksAsyncTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.clearAdapter();
            helper = new DatabaseHelper(AlreadyReadBooksActivity.this);
            database = helper.getReadableDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                cursor = database.query("alreadyReadBooks", null, null, null, null, null, null);
                if (cursor.moveToFirst()) {
                    for (int i = 0; i < cursor.getCount(); i++) {
                        Book book = new Book();
                        for (int j = 0; j < cursor.getColumnCount(); j++) {
                            switch (cursor.getColumnName(j)) {

                                case "_id":
                                    book.setId(cursor.getInt(j));
                                case "title":
                                    book.setName(cursor.getString(j));
                                    break;
                                case "pages":
                                    book.setPages(cursor.getInt(j));
                                    break;
                                case "imageUrl":
                                    book.setImageUrl(cursor.getString(j));
                                    break;
                                case "author":
                                    book.setAuthor(cursor.getString(j));
                                    break;
                                case "shortDescription":
                                    book.setShortDescription(cursor.getString(j));
                                    break;
                                case "longDescription":
                                    book.setLongDescription(cursor.getString(j));
                                    break;
                                case "isLiked":
                                    int isFavorite = cursor.getInt(j);
                                    if (isFavorite == 1) {
                                        book.setLiked(true);
                                    } else {
                                        book.setLiked(false);
                                    }
                                default:
                                    break;
                            }
                        }
                        alreadyReadBooks.add(book);
                        cursor.moveToNext();
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.setBooksArrayList(alreadyReadBooks);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.close_in,R.anim.close_out);
    }
}