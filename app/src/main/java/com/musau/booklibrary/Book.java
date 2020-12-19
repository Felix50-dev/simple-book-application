package com.musau.booklibrary;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable{
    private int id;
    private String name;
    private int pages;
    private String imageUrl;
    private String author;
    private String shortDescription;
    private String longDescription;
    private boolean isLiked ;

    public Book() {
    }

    protected Book(Parcel in) {
        id = in.readInt();
        name = in.readString();
        pages = in.readInt();
        imageUrl = in.readString();
        author = in.readString();
        shortDescription = in.readString();
        longDescription = in.readString();
        isLiked = in.readByte() != 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Book(String name, int pages, String imageUrl, String author, String shortDescription, String longDescription) {
        this.name = name;
        this.pages = pages;
        this.imageUrl = imageUrl;
        this.author = author;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeInt(pages);
        parcel.writeString(imageUrl);
        parcel.writeString(author);
        parcel.writeString(shortDescription);
        parcel.writeString(longDescription);
        parcel.writeByte((byte) (isLiked ? 1 : 0));
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", pages=" + pages +
                ", imageUrl='" + imageUrl + '\'' +
                ", author='" + author + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                ", isLiked=" + isLiked +
                '}';
    }
}
