package com.musau.booklibrary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookRecyclerViewAdapter extends RecyclerView.Adapter<BookRecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "BookRecyclerViewAdapter";

    private ArrayList<Book> booksArrayList = new ArrayList<>();
    private Context context;
    private DatabaseHelper helper;

    interface OnDeletingBook{
        void onDeletingBookResult(Book book);
    }

    private OnDeletingBook onDeletingBook;

    public BookRecyclerViewAdapter(Context context) {
        this.context = context;
        helper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.book_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: started");

        holder.textTitle.setText(booksArrayList.get(position).getName());
        holder.txtShortDescription.setText(booksArrayList.get(position).getShortDescription());

        Glide.with(context)
                .asBitmap()
                .load(booksArrayList.get(position).getImageUrl())
                .into(holder.imageView);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,BookActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("incomingBook",booksArrayList.get(position));
                intent.putExtra("bundle",bundle);
                context.startActivity(intent);
            }
        });

        holder.parent.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context)
                        .setTitle("Deleting " + booksArrayList.get(position).getName())
                        .setMessage("Are you sure you want to delete " + booksArrayList.get(position).getName() + "?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                try {
                                    onDeletingBook = (OnDeletingBook) context;
                                    onDeletingBook.onDeletingBookResult(booksArrayList.get(position));
                                }catch (ClassCastException e){
                                    e.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //dismiss
                            }
                        });
                builder.create().show();
                return true;
            }
        });
    }

    public void clearAdapter(){
        this.booksArrayList.clear();
        notifyDataSetChanged(); 
    }

    @Override
    public int getItemCount() {
        return booksArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CardView parent;
        private TextView textTitle,txtShortDescription;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            parent = itemView.findViewById(R.id.parent);
            textTitle = itemView.findViewById(R.id.txtBookTitle);
            txtShortDescription = itemView.findViewById(R.id.txtShortDescription);
            imageView = itemView.findViewById(R.id.bookImage);
        }
    }

    public void setBooksArrayList(ArrayList<Book> booksArrayList) {
        this.booksArrayList = booksArrayList;
        notifyDataSetChanged();
    }
}
