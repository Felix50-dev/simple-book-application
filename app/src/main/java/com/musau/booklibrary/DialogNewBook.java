package com.musau.booklibrary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class DialogNewBook extends DialogFragment {
    private static final String TAG = "DialogNewBook";

    private EditText editTextName,editTextAuthor,editTextPages,editTextShortDesc,editTextLongDesc,editTextImageUrl;
    private Button btnAdd,btnCancel;

    interface AddNewBook{
        void onAddingNewBookResult(Book book);
    }
    private AddNewBook addNewBook;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_new_book,null,false);
        initViews(view);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setView(view);

        DatabaseHelper helper = new DatabaseHelper(getActivity());
        SQLiteDatabase sqLiteDatabase = helper.getWritableDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Book book = new Book();

                book.setName(editTextName.getText().toString());
                book.setAuthor(editTextAuthor.getText().toString());
                book.setPages(Integer.parseInt(editTextPages.getText().toString()));
                book.setImageUrl(editTextImageUrl.getText().toString());
                book.setShortDescription(editTextShortDesc.getText().toString());
                book.setLongDescription(editTextLongDesc.getText().toString());

                try{
                    addNewBook = (AddNewBook)getActivity();
                    addNewBook.onAddingNewBookResult(book);
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return builder.create();
    }

    private void initViews(View view){
        editTextName = view.findViewById(R.id.editTxtName);
        editTextAuthor = view.findViewById(R.id.editTxtAuthor);
        editTextPages = view.findViewById(R.id.editTxtPages);
        editTextShortDesc = view.findViewById(R.id.editTxtShortDescription);
        editTextLongDesc = view.findViewById(R.id.editTxtLongDescription);
        editTextImageUrl = view.findViewById(R.id.editTxtImageUrl);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCancel = view.findViewById(R.id.btnCancel);
    }
}
