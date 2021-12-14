package com.example.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    //Angabe Klassenname für spätere Logausgabe
    private static final String LOG_TAG = DBHelper.class.getSimpleName();
    //Variable für DB Name
    private static final String DB_NAME = "todo_list.db3";
    //Varible für DB Version (wichtig um DB auf Endgeräten bei Bedraf aktualisieren zu können)
    private static final int DB_VERSION = 1;
    //Variable für den Tabellennamen
    static final String TABLE_TODO_LIST = "todo_list";
    //Variable für Tabellenspalten
    static final String COLUMN_ID = "_id";
    static final String COLUMN_TASKCONTENT = "Titel";
    static final String COLUMN_TASKSTATUS = "Status";
    static final String COLUMN_CREATEDATE = "Erstelldatum";

    //String zum erstellen der Tabelle
    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_TODO_LIST +
                    " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TASKCONTENT + " TEXT NOT NULL, " +
                    COLUMN_TASKSTATUS + " BOOLEAN DEFAULT 0, " +
                    COLUMN_CREATEDATE + " TEXT NOT NULL);";
    //String zum löschen der Tabelle
    private static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_TODO_LIST;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DBHelper hat die Datenbank: " + getDatabaseName() + " erzeugt");
    }

    //Wird immer dann aufgerufen, wenn es noch keine Datenbank gibt
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            Log.d(LOG_TAG, "Die Tabelle wird mit dem Befehl: " + SQL_CREATE + " angelegt");
            sqLiteDatabase.execSQL(SQL_CREATE);
        }
        catch(Exception ex){
            Log.e(LOG_TAG, "Fehler beim anlegen der Tabelle: " + ex.getMessage());
        }
    }

    //Wird aufgerufen, sobald die neue Versionsnummer größer als die alte ist.
    //und somit ein Upgrade der Datenbank notwendig ist
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.d(LOG_TAG, "Die Tabelle mit der Versionsnummer " + oldVersion + " wird entfernt");
        sqLiteDatabase.execSQL(SQL_DROP);
        onCreate(sqLiteDatabase);

        //Um die Datensätze zu erhalten, müssten diese vorher in eine temporäre Tabelle gespeichert
        //werden und die Tabelle neu gebaut werden und alle Spalten, die neu hinzugekommen sind für
        //die bisherigen Datensätze befüllt werden, falls diese Spalten nicht leer sein dürfen.
    }
}
