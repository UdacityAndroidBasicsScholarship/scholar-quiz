package org.sairaa.scholarquiz.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.sairaa.scholarquiz.data.QuizContract.*;

public class QuizProvider extends ContentProvider{



    /** Tag for the log messages */
    public static final String LOG_QUIZ_PROVIDER = QuizProvider.class.getName();
    //database helper object
    private QuizDbHelper mDbHelper;

    private static final int SUBSCRIBE = 100;
    private static final int SUBSCRIBE_ID = 101;

    private static final int LESSONQUIZ = 200;
    private static final int LESSONQUIZ_ID = 201;

    private static final int QUIZ = 300;
    private static final int QUIZ_ID = 301;

    private static final int SCOREBOARD = 400;
    private static final int SCOREBOARD_ID = 401;
    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    // Static initializer. This is run the first time anything is called from this class.
    static {
        // URI MATCHERS
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_SUBSCRIBE,SUBSCRIBE);
        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY,QuizContract.PATH_SUBSCRIBE +"/#",SUBSCRIBE_ID);

        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_LESSONQUIZ,LESSONQUIZ);
        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_LESSONQUIZ + "/#",LESSONQUIZ_ID);

        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_QUIZ,QUIZ);
        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_QUIZ+"/#",QUIZ_ID);

        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_SCOREBOARD,SCOREBOARD);
        sUriMatcher.addURI(QuizContract.CONTENT_AUTHORITY, QuizContract.PATH_SCOREBOARD+"/#",SCOREBOARD_ID);



    }

    @Override
    public boolean onCreate() {

        mDbHelper = new QuizDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        // This cursor will hold the result of the query
        Cursor cursor = null;

        // Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);

        switch (match){

            case SUBSCRIBE :
                cursor = database.query(subscriptionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case SUBSCRIBE_ID:
                selection = subscriptionEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                // This will perform a query on the subscribe table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(subscriptionEntry.TABLE_NAME, projection, selection, selectionArgs,
                    null, null, sortOrder);

                break;

            case LESSONQUIZ :
                cursor = database.query(lessonQuizEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case  LESSONQUIZ_ID:

                selection = lessonQuizEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(lessonQuizEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case QUIZ :
                cursor = database.query(quizQuestionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case QUIZ_ID:

                selection = quizQuestionEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(quizQuestionEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;
            case SCOREBOARD:

                cursor = database.query(scoreBoardEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

            case SCOREBOARD_ID:

                selection = scoreBoardEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = database.query(scoreBoardEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);

                break;

             default:
                 throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);

        switch (match){
            case SUBSCRIBE:
                return insertSubscription(uri, contentValues);

            case LESSONQUIZ:

                return insertLessonQuiz(uri, contentValues);
            case QUIZ:
                return insertQuizQuestion(uri, contentValues);

            case SCOREBOARD:
                return insertScoreBoard(uri, contentValues);

            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }

    }

    private Uri insertScoreBoard(Uri uri, ContentValues contentValues) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();




        // Insert the new pet with the given values
        long id = database.insert(scoreBoardEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_QUIZ_PROVIDER, "Failed to insert in scoreboard row for " + uri);
            return null;
        }
        //Notify all listner when data has changed
        getContext().getContentResolver().notifyChange(uri,null);
        // Return the new URI with the ID (of the newly inserted row) appended at the end

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertLessonQuiz(Uri uri, ContentValues contentValues) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String name = contentValues.getAsString(lessonQuizEntry.Q_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Lesson Quiz requires a name");
        }

        // Insert the new pet with the given values
        long id = database.insert(lessonQuizEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_QUIZ_PROVIDER, "Failed to insert in LessonQuiz row for " + uri);
            return null;
        }

        //Notify all listner when data has changed
        getContext().getContentResolver().notifyChange(uri,null);
        // Return the new URI with the ID (of the newly inserted row) appended at the end

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertQuizQuestion(Uri uri, ContentValues contentValues) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String name = contentValues.getAsString(quizQuestionEntry.QUESTION);
        if (name == null) {
            throw new IllegalArgumentException("question requires a name");
        }

        // Insert the new pet with the given values
        long id = database.insert(quizQuestionEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_QUIZ_PROVIDER, "Failed to insert in quiz table row for " + uri);
            return null;
        }
        //Notify all listner when data has changed
        getContext().getContentResolver().notifyChange(uri,null);
        // Return the new URI with the ID (of the newly inserted row) appended at the end

        return ContentUris.withAppendedId(uri, id);
    }

    private Uri insertSubscription(Uri uri, ContentValues contentValues) {

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        String name = contentValues.getAsString(subscriptionEntry.L_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Pet requires a name");
        }

        // Insert the new pet with the given values
        long id = database.insert(subscriptionEntry.TABLE_NAME, null, contentValues);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_QUIZ_PROVIDER, "Failed to insert row for " + uri);
            return null;
        }
        //Notify all listner when data has changed
        getContext().getContentResolver().notifyChange(uri,null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        int rowsDeleted;
        switch (match){
            case SUBSCRIBE:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(subscriptionEntry.TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            case  SUBSCRIBE_ID:
                // Delete a single row given by the ID in the URI
                selection = subscriptionEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(subscriptionEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case LESSONQUIZ:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(lessonQuizEntry.TABLE_NAME, selection, selectionArgs);

                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case  LESSONQUIZ_ID:
                // Delete a single row given by the ID in the URI
                selection = lessonQuizEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(lessonQuizEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            case QUIZ:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(quizQuestionEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case  QUIZ_ID:
                // Delete a single row given by the ID in the URI
                selection = quizQuestionEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(quizQuestionEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;

            case SCOREBOARD:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(scoreBoardEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            case  SCOREBOARD_ID:
                // Delete a single row given by the ID in the URI
                selection = scoreBoardEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(scoreBoardEntry.TABLE_NAME, selection, selectionArgs);
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match){
            case SUBSCRIBE:
                return updateSubscribe(uri, contentValues, selection, selectionArgs);

            case SUBSCRIBE_ID:

                selection = subscriptionEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateSubscribe(uri, contentValues, selection, selectionArgs);

            case LESSONQUIZ:
                return updateLessonQuiz(uri, contentValues, selection, selectionArgs);

            case LESSONQUIZ_ID:
                selection = lessonQuizEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateLessonQuiz(uri, contentValues, selection, selectionArgs);

            case QUIZ:
                return updateQuizQuestion(uri, contentValues, selection, selectionArgs);

            case QUIZ_ID:
                selection = quizQuestionEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateQuizQuestion(uri, contentValues, selection, selectionArgs);

            case SCOREBOARD:
                return updateScoreBoard(uri, contentValues, selection, selectionArgs);

            case SCOREBOARD_ID:
                selection = scoreBoardEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateScoreBoard(uri, contentValues, selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);    

        }


    }

    private int updateScoreBoard(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(scoreBoardEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }

    private int updateQuizQuestion(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(quizQuestionEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }

    private int updateLessonQuiz(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        if (contentValues.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(lessonQuizEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    private int updateSubscribe(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        if (contentValues.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(subscriptionEntry.TABLE_NAME, contentValues, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;

    }
}
