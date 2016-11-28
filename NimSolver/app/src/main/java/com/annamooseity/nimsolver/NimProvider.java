package com.annamooseity.nimsolver;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;


/**
 * NimProvider.java
 * Anna Carrigan
 * Content Provider for our Nim game
 */
public class NimProvider extends ContentProvider
{
    // Database name and the big info
    private static final String DATABASE_NAME = "nimProvider.db";
    private static final int DATABASE_VERSION = 1;
    public static final String PROVIDER = "com.annamooseity.nim";

    // Table names
    private static final String GAME_TABLE_NAME = "games";
    private static final String RULES_TABLE_NAME = "rules";

    // Stuff for matching URIs
    private static final UriMatcher sUriMatcher;
    private static HashMap<String, String> gameProjectionMap;
    private static HashMap<String, String> rulesProjectionMap;

    // Integer IDs for the URI matcher
    private static final int GAME = 1;
    private static final int GAME_ID = 2;
    private static final int RULES = 3;
    private static final int RULES_ID = 4;

    // Helper for database
    private DatabaseHelper dbHelper;

    // The Database Helper
    private static class DatabaseHelper extends SQLiteOpenHelper
    {
        DatabaseHelper(Context context)
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            // Create NimRules table
            db.execSQL("CREATE TABLE " + RULES_TABLE_NAME + " (" +
                    NimRules.RULES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NimRules.PILES + " TEXT," +
                    NimRules.TAKE_OPTIONS + " TEXT," +
                    NimRules.PLAYER_FIRST + " TEXT" + ");");

            // Create NimGame table
            db.execSQL("CREATE TABLE " + GAME_TABLE_NAME + " (" +
                    NimGame.GAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NimGame.PILES + " INTEGER," +
                    NimGame.RULES_INDEX + " INTEGER," +
                    NimGame.OPPONENT + " TEXT," +
                    NimGame.MOVE + " TEXT" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            db.execSQL("DROP TABLE IF EXISTS " + RULES_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + GAME_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, String where, String[] whereArgs)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        Uri notifyUri;
        switch (sUriMatcher.match(uri))
        {
            case GAME:
            case GAME_ID:
                notifyUri = NimGame.CONTENT_URI_game;
                count = db.delete(GAME_TABLE_NAME, where, whereArgs);
                break;
            case RULES_ID:
            case RULES:
                notifyUri = NimRules.CONTENT_URI_rules;
                count = db.delete(RULES_TABLE_NAME, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(notifyUri, null);
        }
        return count;
    }

    @Override
    public String getType(@NonNull Uri uri) throws IllegalArgumentException
    {
        switch (sUriMatcher.match(uri))
        {
            case GAME:
                return NimGame.CONTENT_TYPE;
            case GAME_ID:
                return NimGame.CONTENT_TYPE;
            case RULES:
                return NimRules.CONTENT_TYPE;
            case RULES_ID:
                return NimRules.CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues initialValues)
    {

        ContentValues values;
        if (initialValues != null)
        {
            values = new ContentValues(initialValues);
        }
        else
        {
            values = new ContentValues();
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long rowId;

        switch (sUriMatcher.match(uri))
        {
            case GAME:
            case GAME_ID:
                rowId = db.insert(GAME_TABLE_NAME, NimGame.PILES, values);
                if (rowId > 0)
                {
                    Uri gameUri = ContentUris.withAppendedId(NimGame.CONTENT_URI_game, rowId);
                    if(getContext() != null) {
                        getContext().getContentResolver().notifyChange(gameUri, null);
                    }
                    return gameUri;
                }
                break;
            case RULES:
            case RULES_ID:
                rowId = db.insert(RULES_TABLE_NAME, NimRules.PILES, values);
                if (rowId > 0)
                {
                    Uri rulesUri = ContentUris.withAppendedId(NimRules.CONTENT_URI_rules, rowId);
                    if(getContext() != null) {
                        getContext().getContentResolver().notifyChange(rulesUri, null);
                    }
                    return rulesUri;
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public boolean onCreate()
    {
        dbHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        Uri notifyUri;

        switch (sUriMatcher.match(uri))
        {
            case GAME:
                qb.setTables(GAME_TABLE_NAME);
                qb.setProjectionMap( gameProjectionMap);
                notifyUri = NimGame.CONTENT_URI_game;
                break;
            case GAME_ID:
                qb.setTables(GAME_TABLE_NAME);
                qb.setProjectionMap( gameProjectionMap);
              notifyUri = NimGame.CONTENT_URI_game;
                break;
            case RULES:
                qb.setTables(RULES_TABLE_NAME);
                qb.setProjectionMap(rulesProjectionMap);
                notifyUri = NimRules.CONTENT_URI_rules;
                break;
            case RULES_ID:
                qb.setTables(RULES_TABLE_NAME);
                qb.setProjectionMap(rulesProjectionMap);
                notifyUri = NimRules.CONTENT_URI_rules;
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        if(getContext() != null) {
            c.setNotificationUri(getContext().getContentResolver(), notifyUri);
        }

        return c;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String where, String[] whereArgs)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int count;
        Uri notifyUri;
        switch (sUriMatcher.match(uri))
        {
            case GAME:
            case GAME_ID:
                notifyUri = NimGame.CONTENT_URI_game;
                count = db.update(GAME_TABLE_NAME, values, where, whereArgs);
                break;
            case RULES:
            case RULES_ID:
                notifyUri = NimRules.CONTENT_URI_rules;
                count = db.update(RULES_TABLE_NAME, values, where, whereArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        if(getContext() != null) {
            getContext().getContentResolver().notifyChange(notifyUri, null);
        }
        return count;
    }

    /**
     * Add the projection maps for the database
     */
    static
    {
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(PROVIDER, GAME_TABLE_NAME, GAME);
        sUriMatcher.addURI(PROVIDER, GAME_TABLE_NAME + "/#", GAME_ID);
        sUriMatcher.addURI(PROVIDER, RULES_TABLE_NAME, RULES);
        sUriMatcher.addURI(PROVIDER, RULES_TABLE_NAME + "/#", RULES_ID);

        gameProjectionMap = new HashMap<>();
        gameProjectionMap.put(NimGame.GAME_ID, NimGame.GAME_ID);
        gameProjectionMap.put(NimGame.PILES, NimGame.PILES);
        gameProjectionMap.put(NimGame.RULES_INDEX, NimGame.RULES_INDEX);
        gameProjectionMap.put(NimGame.MOVE, NimGame.MOVE);
        gameProjectionMap.put(NimGame.OPPONENT, NimGame.OPPONENT);

         rulesProjectionMap = new HashMap<>();
         rulesProjectionMap.put(NimRules.RULES_ID, NimRules.RULES_ID);
         rulesProjectionMap.put(NimRules.PILES, NimRules.PILES);
         rulesProjectionMap.put(NimRules.PLAYER_FIRST, NimRules.PLAYER_FIRST);
         rulesProjectionMap.put(NimRules.TAKE_OPTIONS, NimRules.TAKE_OPTIONS);

    }
}
