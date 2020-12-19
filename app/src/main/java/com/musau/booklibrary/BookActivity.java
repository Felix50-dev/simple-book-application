package com.musau.booklibrary;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class BookActivity extends AppCompatActivity {
    private static final String TAG = "BookActivity";

    private TextView txtTitle, txtShortDescription, txtPages, txtAuthor, txtLongDescription, txtLanguage;
    private ImageView imageViewBook, imageViewIcon, imageViewEmptyStar;
    private Button btnAddToLibrary,btnAlreadyReadBook;
    private DatabaseHelper helper;
    private SQLiteDatabase database;
    private Cursor cursor;
    private Book incomingBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        initViews();

        try {
            Intent intent = getIntent();
            Bundle bundle = intent.getBundleExtra("bundle");

            if (null != bundle) {
                incomingBook = bundle.getParcelable("incomingBook");

                txtTitle.setText(incomingBook.getName());
                txtShortDescription.setText(incomingBook.getShortDescription());
                txtPages.setText(String.valueOf(incomingBook.getPages()));
                txtAuthor.setText(incomingBook.getAuthor());
                txtLongDescription.setText(incomingBook.getLongDescription());
                txtLanguage.setText("English");

                btnAddToLibrary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        helper.insertBookToLibrary(database,incomingBook);
                    }
                });
                btnAlreadyReadBook.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BookActivity.this)
                                .setMessage("Have you finished reading " + incomingBook.getName() + "?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        helper.deleteBookFromLibraryByName(database,incomingBook);
                                        helper.insertBookToAlreadyReadBooks(database,incomingBook);
                                    }
                                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                });
                        builder.create().show();
                    }
                });

                Glide.with(this)
                        .asBitmap()
                        .load(incomingBook.getImageUrl())
                        .into(imageViewBook);

                imageViewIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        incomingBook.setLiked(false);
                        helper.updateLike(database, incomingBook);
                        imageViewIcon.setVisibility(View.GONE);
                        imageViewEmptyStar.setVisibility(View.VISIBLE);
                    }
                });
                imageViewEmptyStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        incomingBook.setLiked(true);
                        helper.updateLike(database, incomingBook);
                        imageViewEmptyStar.setVisibility(View.GONE);
                        imageViewIcon.setVisibility(View.VISIBLE);
                    }
                });

                cursor = database.query("books", new String[]{"isLiked"}, "_id = ?", new String[]{String.valueOf(incomingBook.getId())}, null, null, null);

                if (cursor.moveToFirst()) {
                    int isLiked = cursor.getInt(0);
                    if (isLiked == 1) {
                        imageViewEmptyStar.setVisibility(View.GONE);
                        imageViewIcon.setVisibility(View.VISIBLE);
                    } else {
                        imageViewIcon.setVisibility(View.GONE);
                        imageViewEmptyStar.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.d(TAG, "onCreate: errrrrooorrr");
                }
            } else {
                Log.d(TAG, "onCreate: bundle == null");
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        txtTitle = findViewById(R.id.txtTitle);
        txtShortDescription = findViewById(R.id.txtViewShortDescription);
        txtAuthor = findViewById(R.id.txtViewAuthor);
        txtPages = findViewById(R.id.txtViewPages);
        txtLanguage = findViewById(R.id.txtViewLanguage);
        txtLongDescription = findViewById(R.id.txtLongDesc);

        btnAddToLibrary = findViewById(R.id.btnAddToLibrary);
        btnAlreadyReadBook = findViewById(R.id.btnAlreadyRead);

        imageViewBook = findViewById(R.id.imageView);
        imageViewIcon = findViewById(R.id.imageViewIcon);
        imageViewEmptyStar = findViewById(R.id.imageViewEmptyStar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        database.close();
    }
}