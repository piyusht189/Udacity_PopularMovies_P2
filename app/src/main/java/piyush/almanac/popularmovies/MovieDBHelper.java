package piyush.almanac.popularmovies;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION= 2;
    public static final String DATABASE_NAME= "movie.db";

    final String CREATE_MOVIE_TABLE="CREATE TABLE " + DBParameters.MOVIE_ENTRY.TABLE_NAME + " ( "
            + DBParameters.MOVIE_ENTRY.COLUMN_ID + " integer primary key autoincrement,"
            + DBParameters.MOVIE_ENTRY.COLUMN_MOVIE_ID + " text not null, "
            + DBParameters.MOVIE_ENTRY.COLUMN_TITLE + " text not null,"
            + DBParameters.MOVIE_ENTRY.COLUMN_USER_RATING + " text not null, "
            + DBParameters.MOVIE_ENTRY.COLUMN_RELEASE_DATE + " text not null, "
            + DBParameters.MOVIE_ENTRY.COLUMN_OVERVIEW + " text not null, "
            + DBParameters.MOVIE_ENTRY.COLUMN_POSTER + " text not null, "
            + DBParameters.MOVIE_ENTRY.COLUMN_BACKDROP + " text not null);";

    public MovieDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        sqLiteDatabase.execSQL(CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ DBParameters.MOVIE_ENTRY.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
