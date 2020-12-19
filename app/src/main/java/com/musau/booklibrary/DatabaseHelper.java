package com.musau.booklibrary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    public static final String DATABASE_NAME = ".bookDatabase.db";
    public static final int DATABASE_VERSION = 1;
    private Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "onCreate: attempting to create database");
        String sqlStatement = "CREATE TABLE books (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "pages INTEGER," +
                "imageUrl TEXT," +
                "author TEXT," +
                "shortDescription TEXT," +
                "longDescription TEXT," +
                "isLiked INTEGER);";
        sqLiteDatabase.execSQL(sqlStatement);
        addBooksToDatabase(sqLiteDatabase);
        String sqlStatement1 = "CREATE TABLE libraryBooks (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "pages INTEGER," +
                "imageUrl TEXT," +
                "author TEXT," +
                "shortDescription TEXT," +
                "longDescription TEXT," +
                "isLiked INTEGER);";
       sqLiteDatabase.execSQL(sqlStatement1);
       String sqlStatement2 = "CREATE TABLE AlreadyReadBooks (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
               "title TEXT," +
               "pages INTEGER," +
               "imageUrl TEXT," +
               "author TEXT," +
               "shortDescription TEXT," +
               "longDescription TEXT," +
               "isLiked INTEGER);";
       sqLiteDatabase.execSQL(sqlStatement2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void insertBook(SQLiteDatabase db,Book book){
        Log.d(TAG, "insert: started");

        ContentValues contentValues = new ContentValues();
        contentValues.put("title",book.getName());
        contentValues.put("pages",book.getPages());
        contentValues.put("imageUrl",book.getImageUrl());
        contentValues.put("author",book.getAuthor());
        contentValues.put("shortDescription",book.getShortDescription());
        contentValues.put("longDescription",book.getLongDescription());

        if(book.isLiked()){
            contentValues.put("isLiked",1);
        }else{
            contentValues.put("isLiked",-1);
        }

        db.insert("books",null,contentValues);
    }

    public boolean insertBookToLibrary(SQLiteDatabase db,Book book){
        Log.d(TAG, "insert: started");

        Cursor cursor = db.query("libraryBooks",new String[]{"title"},"title = ?",new String[]{book.getName()},null,null,null);
        if(cursor.moveToFirst()){
            for(int i = 0;i < cursor.getCount();i++){
                if(cursor.getString(i).equals(book.getName())){
                    Toast.makeText(context,"book already added to library ",Toast.LENGTH_SHORT).show();
                }
                cursor.moveToNext();
            }
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("title",book.getName());
        contentValues.put("pages",book.getPages());
        contentValues.put("imageUrl",book.getImageUrl());
        contentValues.put("author",book.getAuthor());
        contentValues.put("shortDescription",book.getShortDescription());
        contentValues.put("longDescription",book.getLongDescription());

        if(book.isLiked()){
            contentValues.put("isLiked",1);
        }else{
            contentValues.put("isLiked",-1);
        }

        db.insert("libraryBooks",null,contentValues);
        Toast.makeText(context,book.getName() + " added to library", Toast.LENGTH_SHORT).show();
        cursor.close();
        return true;
    }

    public boolean insertBookToAlreadyReadBooks(SQLiteDatabase db,Book book){
        Log.d(TAG, "insert: started");

        Cursor cursor = db.query("AlreadyReadBooks",new String[]{"title"},"title = ?",new String[]{book.getName()},null,null,null);
        if(cursor.moveToFirst()){
            for(int i = 0;i < cursor.getCount();i++){
                if(cursor.getString(i).equals(book.getName())){
                    Toast.makeText(context,"book already in read list",Toast.LENGTH_SHORT).show();
                }
                cursor.moveToNext();
            }
            return false;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put("title",book.getName());
        contentValues.put("pages",book.getPages());
        contentValues.put("imageUrl",book.getImageUrl());
        contentValues.put("author",book.getAuthor());
        contentValues.put("shortDescription",book.getShortDescription());
        contentValues.put("longDescription",book.getLongDescription());

        if(book.isLiked()){
            contentValues.put("isLiked",1);
        }else{
            contentValues.put("isLiked",-1);
        }

        db.insert("alreadyReadBooks",null,contentValues);
        Toast.makeText(context,book.getName() + " added to already read list", Toast.LENGTH_SHORT).show();
        cursor.close();
        return true;
    }

    public void updateLike(SQLiteDatabase db,Book book){
        ContentValues contentValues = new ContentValues();

        if(book.isLiked()){
            contentValues.put("isLiked",1);
        }else{
            contentValues.put("isLiked",-1);
        }

        db.update("books",contentValues,"_id = ?",new String[]{String.valueOf(book.getId())});
    }
    public void deleteBook(SQLiteDatabase db,Book book){
        db.delete("books","_id = ?",new String[]{String.valueOf(book.getId())});
    }

    public void deleteBookFromLibrary(SQLiteDatabase db,Book book){
        db.delete("libraryBooks","_id = ?",new String[]{String.valueOf(book.getId())});
    }

    public void deleteBookFromLAlreadyReadBooks(SQLiteDatabase db,Book book){
        db.delete("alreadyReadBooks","_id = ?",new String[]{String.valueOf(book.getId())});
    }

    public void deleteBookFromLibraryByName(SQLiteDatabase db,Book book){
        db.delete("LibraryBooks","title = ?",new String[]{book.getName()});
    }

    private void addBooksToDatabase(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "addBooksToDatabase: started");

        String name;
        int pages;
        String imageUrl;
        String author;
        String shortDescription;
        String longDescription;


        name = "Mere Christianity";
        pages = 560;
        imageUrl = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1531409863l/40792344._SX318_.jpg";
        author = "C.S LEWIS";
        shortDescription = "";
        longDescription = "Mere Christianity is C.S. Lewis's forceful and accessible doctrine of Christian belief. First heard as informal radio broadcasts and then published as three separate books - The Case for Christianity, Christian Behavior, and Beyond Personality - Mere Christianity brings together what Lewis saw as the fundamental truths of the religion. Rejecting the boundaries that divide Christianity's many denominations, C.S. Lewis finds a common ground on which all those who have Christian faith can stand together, proving that at the centre of each there is something, or a Someone, who against all divergences of belief, all differences of temperament, all memories of mutual persecution, speaks the same voice.";

        Book book1 = new Book(name, pages, imageUrl, author, shortDescription, longDescription);
        insertBook(sqLiteDatabase, book1);

        name = "1984";
        pages = 350;
        imageUrl = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1532714506l/40961427._SX318_.jpg";
        author = "George Orwel";
        shortDescription = "";
        longDescription = "Among the seminal texts of the 20th century, Nineteen Eighty-Four is a rare work that grows more haunting as its futuristic purgatory becomes more real. Published in 1949, the book offers political satirist George Orwell's nightmarish vision of a totalitarian, bureaucratic world and one poor stiff's attempt to find individuality. The brilliance of the novel is Orwell's prescience of modern life—the ubiquity of television, the distortion of the language—and his ability to construct such a thorough version of hell. Required reading for students since it was published, it ranks among the most terrifying novels ever written";

        Book book2 = new Book(name, pages, imageUrl, author, shortDescription, longDescription);
        insertBook(sqLiteDatabase, book2);

        name = "The boys in the boat";
        pages = 633;
        imageUrl = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1354683116l/16158542.jpg";
        author = "Daniel James Brown";
        shortDescription = "";
        longDescription = "For readers of Laura Hillenbrand's Seabiscuit and Unbroken, the dramatic story of the American rowing team that stunned the world at Hitler's 1936 Berlin Olympics.Daniel James Brown's robust book tells the story of the University of Washington's 1936 eight-oar crew and their epic quest for an Olympic gold medal, a team that transformed the sport and grabbed the attention of millions of Americans. The sons of loggers, shipyard workers, and farmers, the boys defeated elite rivals first from eastern and British universities and finally the German crew rowing for Adolf Hitler in the Olympic games in Berlin, 1936.The emotional heart of the story lies with one rower, Joe Rantz, a teenager without family or prospects, who rows not for glory, but to regain his shattered self-regard and to find a place he can call home. The crew is assembled by an enigmatic coach and mentored by a visionary, eccentric British boat builder, but it is their trust in each other that makes them a victorious team. They remind the country of what can be done when everyone quite literally pulls together—a perfect melding of commitment, determination, and optimism.Drawing on the boys' own diaries and journals, their photos and memories of a once-in-a-lifetime shared dream, The Boys in the Boat is an irresistible story about beating the odds and finding hope in the most desperate of times—the improbable, intimate story of nine working-class boys from the American west who, in the depths of the Great Depression, showed the world what true grit really meant. It will appeal to readers of Erik Larson, Timothy Egan, James Bradley, and David Halberstam's The Amateurs.";

        Book book3 = new Book(name, pages, imageUrl, author, shortDescription, longDescription);
        insertBook(sqLiteDatabase, book3);

        name = "The Diary of a Young Girl";
        author = "Anne Frank";
        pages = 312;
        imageUrl = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1560816565l/48855.jpg";
        shortDescription = "";
        longDescription = "Discovered in the attic in which she spent the last years of her life, Anne Frank’s remarkable diary has become a world classic—a powerful reminder of the horrors of war and an eloquent testament to the human spirit. In 1942, with the Nazis occupying Holland, a thirteen-year-old Jewish girl and her family fled their home in Amsterdam and went into hiding. For the next two years, until their whereabouts were betrayed to the Gestapo, the Franks and another family lived cloistered in the “Secret Annexe” of an old office building. Cut off from the outside world, they faced hunger, boredom, the constant cruelties of living in confined quarters, and the ever-present threat of discovery and death. In her diary Anne Frank recorded vivid impressions of her experiences during this period. By turns thoughtful, moving, and surprisingly humorous, her account offers a fascinating commentary on human courage and frailty and a compelling self-portrait of a sensitive and spirited young woman whose promise was tragically cut short. --back cover";

        Book book4 = new Book(name, pages, imageUrl, author, shortDescription, longDescription);
        insertBook(sqLiteDatabase, book4);

        name = "The Lean StartUp";
        author = "Eric Ries";
        pages = 430;
        imageUrl = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1333576876l/10127019.jpg";
        shortDescription = "";
        longDescription = "Most startups fail. But many of those failures are preventable. The Lean Startup is a new approach being adopted across the globe, changing the way companies are built and new products are launched. Eric Ries defines a startup as an organization dedicated to creating something new under conditions of extreme uncertainty. This is just as true for one person in a garage or a group of seasoned professionals in a Fortune 500 boardroom. What they have in common is a mission to penetrate that fog of uncertainty to discover a successful path to a sustainable business. The Lean Startup approach fosters companies that are both more capital efficient and that leverage human creativity more effectively. Inspired by lessons from lean manufacturing, it relies on validated learning, rapid scientific experimentation, as well as a number of counter-intuitive practices that shorten product development cycles, measure actual progress without resorting to vanity metrics, and learn what customers really want. It enables a company to shift directions with agility, altering plans inch by inch, minute by minute. Rather than wasting time creating elaborate business plans, The Lean Startup offers entrepreneurs - in companies of all sizes - a way to test their vision continuously, to adapt and adjust before it's too late. Ries provides a scientific approach to creating and managing successful startups in a age when companies need to innovate more than ever.";

        Book book5 = new Book(name, pages, imageUrl, author, shortDescription, longDescription);
        insertBook(sqLiteDatabase, book5);

        name = "Charlotte's web";
        author = "E.B White";
        pages = 680;
        imageUrl = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1439632243l/24178._SY475_.jpg";
        shortDescription = "";
        longDescription = "his beloved book by E. B. White, author of Stuart Little and The Trumpet of the Swan, is a classic of children's literature that is just about perfect.This high-quality paperback features vibrant illustrations colorized by Rosemary Wells! Some Pig. Humble. Radiant. These are the words in Charlotte's Web, high up in Zuckerman's barn. Charlotte's spiderweb tells of her feelings for a little pig named Wilbur, who simply wants a friend. They also express the love of a girl named Fern, who saved Wilbur's life when he was born the runt of his litter. E. B. White's Newbery Honor Book is a tender novel of friendship, love, life, and death that will continue to be enjoyed by generations to come. This edition contains newly color illustrations by Garth Williams, the acclaimed illustrator of E. B. White's Stuart Little and Laura Ingalls Wilder's Little House series, among many other books.";

        Book book6 = new Book(name, pages, imageUrl, author, shortDescription, longDescription);
        insertBook(sqLiteDatabase, book6);

        name = "The Smitten Kitchen CookBook";
        author = "Deb Perelman";
        pages = 498;
        imageUrl = "https://i.gr-assets.com/images/S/compressed.photo.goodreads.com/books/1336228481l/13331199.jpg";
        shortDescription = "";
        longDescription = "The long-awaited cookbook by Deb Perelman of Smitten Kitchen—home cook, mom, photographer, and celebrated food blogger.Deb Perelman loves to cook. It’s as simple as that. She isn’t a chef or a restaurant owner—she’s never even waitressed. Cooking in her tiny Manhattan kitchen was, at least at first, for special occasions—and, too often, an unnecessarily daunting venture. Deb found herself overwhelmed by the number of recipes available to her. Have you ever searched for the perfect birthday cake on Google? You’ll get more than three million results. How do you choose? Where do you start? What if you pick a recipe that’s downright bad?So Deb founded her award-winning blog, smittenkitchen.com, on the premise that cooking should be a pleasure, and that the results of your labor can—and should be—delicious...every time. Deb is a firm believer that there are no bad cooks, just bad recipes. She has dedicated herself to finding the best of the best and adapting them for the everyday cook—the ones with little time to spare, little money to burn on unpronounceable ingredients, and little help in the kitchen. And now, with the same warmth, candor, and can-do spirit her blog is known for, Deb presents her first cookbook—more than 100 new recipes, plus a few favorites from her site, all gorgeously illustrated with hundreds of Deb’s beautiful color photographs.The Smitten Kitchen Cookbook is all about approachable, uncompromised home cooking: stepped-up comfort foods, stewy dishes for windy winter afternoons, an apple cake that will answer all questions: “What should my new signature dessert be?” “What is always welcome at a potluck?” “What did Deb consume almost single-handedly a week after having a baby?” These are the recipes you bookmark and use so often they become your own; recipes you slip to a friend who wants to impress her new in-laws; and recipes with simple ingredients that yield amazing results in a minimum amount of time. Deb tells you how to host a brunch and still sleep in—plus what to make for it!—and the essential items you need for your own kitchen. From salads and slaws that make perfect side dishes (or a full meal) to savory tarts and pizzas; from Mushroom Bourguignon to Pancetta, White Bean and Swiss Chard Pot Pies; from Buttered Popcorn Cookies to Chocolate Hazelnut Layer Cake, Deb knows just the thing for a Tuesday night, or your most special occasion. ";

        Book book7 = new Book(name, pages, imageUrl, author, shortDescription, longDescription);
        insertBook(sqLiteDatabase, book7);

    }
}
