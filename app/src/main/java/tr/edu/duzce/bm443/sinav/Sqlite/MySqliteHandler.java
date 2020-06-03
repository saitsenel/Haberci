package tr.edu.duzce.bm443.sinav.Sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tr.edu.duzce.bm443.sinav.Models.Article;
import tr.edu.duzce.bm443.sinav.Models.Source;

/**
 * Created by mortezasaadat on 9/22/16.
 */
public class MySqliteHandler extends SQLiteOpenHelper {



    // Database Version
    private static final int DATABASE_VERSION = 7;

    // Database Name
    private static final String DATABASE_NAME = "news.db";

    // Article Table Name
    private static final String TABLE_ARTICLE = "article";
    private static final String TABLE_RECY_ARTICLE = "recy_article";

    // Article Table Columns
    private static final String id = "id";
    private static final String author = "author";
    private static final String title = "title";
    private static final String description = "description";
    private static final String url = "url";
    private static final String urlToImage = "urlToImage";
    private static final String publishedAt = "publishedAt";
    private static final String content = "content";


    String CREATE_ARTICLE_TABLE = "CREATE TABLE " + TABLE_ARTICLE + "("
            + id +" INTEGER PRIMARY KEY, "
            + author + " TEXT, "
            + title + " TEXT, "
            + description +  " TEXT, "
            + url +  " TEXT, "
            + urlToImage +  " TEXT, "
            + publishedAt +  " TEXT, "
            + content +  " TEXT "+")";


    String CREATE_RECY_ARTICLE_TABLE = "CREATE TABLE " + TABLE_RECY_ARTICLE + "("
            + id +" INTEGER, "
            + author + " TEXT, "
            + title + " TEXT, "
            + description +  " TEXT, "
            + url +  " TEXT, "
            + urlToImage +  " TEXT, "
            + publishedAt +  " TEXT, "
            + content +  " TEXT "+")";

    public MySqliteHandler(Context context) {


        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_ARTICLE_TABLE);
        db.execSQL(CREATE_RECY_ARTICLE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECY_ARTICLE);
        onCreate(db);

    }


    // All Database Operations: create, read, update, delete

    // create
    public void addArticleList(List<Article> articleList) {

        int pos = 0;


        for (Article article : articleList){
            Log.d("dfdfdfdf", article.getPublishedAt());
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(id, pos);
            values.put(author, article.getAuthor());
            values.put(title, article.getTitle());
            values.put(description, article.getDescription());
            values.put(url, article.getUrl());
            values.put(urlToImage, article.getUrlToImage());
            values.put(publishedAt, article.getPublishedAt());
            values.put(content, article.getContent());


            database.replace(TABLE_ARTICLE, null, values);
            pos++;
            database.close();

        }


    }

    public void addRecyArticleList(List<Article> articleList) {

        int pos = 0;


        for (Article article : articleList){
            Log.d("dfdfdfdf", article.getPublishedAt());
            SQLiteDatabase database = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(id, pos);
            values.put(author, article.getAuthor());
            values.put(title, article.getTitle());
            values.put(description, article.getDescription());
            values.put(url, article.getUrl());
            values.put(urlToImage, article.getUrlToImage());
            values.put(publishedAt, article.getPublishedAt());
            values.put(content, article.getContent());


            database.insert(TABLE_RECY_ARTICLE, null, values);
            pos++;
            database.close();

        }


    }





    // Getting a single article - read
    public Article getArticle(int pos) {

        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.query(TABLE_ARTICLE, new String[]{id,
                        author, title, description, url, urlToImage, publishedAt, content},
                id + "=?", new String[]{String.valueOf(pos)}, null, null, null);


        if (cursor != null)  {
            cursor.moveToFirst();
        }


        Article article = new Article();
        article.setAuthor(cursor.getString(1));
        article.setTitle(cursor.getString(2));
        article.setDescription(cursor.getString(3));
        article.setUrl(cursor.getString(4));
        article.setUrlToImage(cursor.getString(5));
        article.setPublishedAt(cursor.getString(6));
        article.setContent(cursor.getString(7));
        return article;


    }



    // Getting all article Objects
    public List<Article> getAllHeadLine() {

        List<Article> articleList = new ArrayList<>();

        String selectAllQuery = "SELECT * FROM " + TABLE_ARTICLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectAllQuery, null);


        if (cursor.moveToFirst()) {

            do {

                Article article = new Article();
                article.setAuthor(cursor.getString(1));
                article.setTitle(cursor.getString(2));
                article.setDescription(cursor.getString(3));
                article.setUrl(cursor.getString(4));
                article.setUrlToImage(cursor.getString(5));
                article.setPublishedAt(cursor.getString(6));
                article.setContent(cursor.getString(7));


                articleList.add(article);

            } while (cursor.moveToNext());


        }

        return articleList;


    }


    public List<Article> getAllRecyArticles() {

        List<Article> articleList = new ArrayList<>();

        String selectAllQuery = "SELECT * FROM " + TABLE_RECY_ARTICLE;
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery(selectAllQuery, null);


        if (cursor.moveToFirst()) {

            do {

                Article article = new Article();
                article.setAuthor(cursor.getString(1));
                article.setTitle(cursor.getString(2));
                article.setDescription(cursor.getString(3));
                article.setUrl(cursor.getString(4));
                article.setUrlToImage(cursor.getString(5));
                article.setPublishedAt(cursor.getString(6));
                article.setContent(cursor.getString(7));


                articleList.add(article);

            } while (cursor.moveToNext());


        }

        return articleList;


    }

/*

    // Updating a single computer

    public int updateComputer(Computer computer) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPUTER_NAME, computer.getComputerName());
        values.put(COLUMN_COMPUTER_TYPE, computer.getComputerType());

        return database.update(TABLE_COMPUTER, values, COLUMN_ID + " = ? ",
                new String[] {String.valueOf(computer.getId())});


    }
*/



   /* // Deleteing a single computer
    public void deleteComputer(Computer computer) {

        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_COMPUTER, COLUMN_ID + " = ?",
                new String[]{String.valueOf(computer.getId())});
        database.close();


    }*/



  /*  // Getting the number of computers

    public int getComputersCount() {
        String computersCountQuery = "SELECT * FROM " + TABLE_COMPUTER;
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery(computersCountQuery, null);
        cursor.close();

        return cursor.getCount();
    }*/










}
