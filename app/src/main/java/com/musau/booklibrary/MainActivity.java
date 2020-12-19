package com.musau.booklibrary;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements DialogNewBook.AddNewBook, BookRecyclerViewAdapter.OnDeletingBook {
    private static final String TAG = "MainActivity";
    DatabaseHelper databaseHelper;
    private BookRecyclerViewAdapter adapter;
    private ArrayList<Book> bookArrayList;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookArrayList = new ArrayList<>();

        overridePendingTransition(R.anim.in,R.anim.out);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.icon_home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.icon_home:
                        //home activity
                        break;
                    case R.id.icon_library:
                        Intent intent = new Intent(MainActivity.this,LibraryActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case R.id.icon_readBooks:
                        Intent readBooksIntent = new Intent(MainActivity.this,AlreadyReadBooksActivity.class);
                        readBooksIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(readBooksIntent);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        adapter = new BookRecyclerViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DatabaseAsyncTask asyncTask = new DatabaseAsyncTask();
        asyncTask.execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        sqLiteDatabase.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_menu, menu);
        return true;
    }

    // should be default
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add) {
            DialogNewBook newBook = new DialogNewBook();
            newBook.show(getSupportFragmentManager(), "add new Book");
        }
        return true;
    }

    @Override
    public void onAddingNewBookResult(Book book) {
        Log.d(TAG, "onAddingNewBookResult: new Book " + book.toString());
        databaseHelper.insertBook(sqLiteDatabase, book);
        DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
        databaseAsyncTask.execute();
    }

    @Override
    public void onDeletingBookResult(Book book) {
        Log.d(TAG, "onDeletingBookResult: new Book " + book.toString());
        databaseHelper.deleteBook(sqLiteDatabase, book);
        DatabaseAsyncTask databaseAsyncTask = new DatabaseAsyncTask();
        databaseAsyncTask.execute();
    }

    public class DatabaseAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.clearAdapter();
            databaseHelper = new DatabaseHelper(MainActivity.this);
            sqLiteDatabase = databaseHelper.getReadableDatabase();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                cursor = sqLiteDatabase.query("books", null, null, null, null, null, null);
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
                        bookArrayList.add(book);
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
            adapter.setBooksArrayList(bookArrayList);
        }
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.close_in,R.anim.close_out);
    }

}