package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataSource {

    private static final String LOG_TAG = DataSource.class.getSimpleName();

    //Variablendeklaration
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private String[] columns = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_TASKCONTENT,
            DBHelper.COLUMN_TASKSTATUS,
            DBHelper.COLUMN_CREATEDATE
    };

    DataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource rezeugt jetzt den dbHelper");
        dbHelper = new DBHelper(context);
    }

    //Schreibender Zugriff auf die Datenbank
    private void open_writeable() {
        Log.d(LOG_TAG, "Eine schreibende Referenz auf die Datenbank wird jetzt angefragt");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbankreferenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    //Lesender Zugriff auf die Datenbank
    private void open_readable() {
        Log.d(LOG_TAG, "Eine lesende Referenz auf die Datenbank wird jetzt angefragt");
        database = dbHelper.getReadableDatabase();
        Log.d(LOG_TAG, "Datenbankreferenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    //Schließen der Datenbank
    private void close_db() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DBHelper geschlossen");
    }

    private ToDo cursorToTask(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(DBHelper.COLUMN_ID);
        int idTaskContent = cursor.getColumnIndex(DBHelper.COLUMN_TASKCONTENT);
        int idTaskStatus = cursor.getColumnIndex(DBHelper.COLUMN_TASKSTATUS);
        int idCreateDate = cursor.getColumnIndex(DBHelper.COLUMN_CREATEDATE);

        int id = cursor.getInt(idIndex);
        String taskContent = cursor.getString(idTaskContent);
        String createDate = cursor.getString(idCreateDate);

        int valueChecked = cursor.getInt(idTaskStatus);
        //boolean status = (valueChecked != 0);

        boolean status = cursor.getInt(idTaskStatus) == 0 ? false : true;

        return new ToDo(id, taskContent, status, createDate);
    }

    ToDo insertTask(ToDo task){
        //Aufbau der Parameter, die in die DB geschrieben werden sollen
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TASKCONTENT, task.getTodo());
        values.put(DBHelper.COLUMN_CREATEDATE, task.getTime());

        //Öffnen der Datenbank mit schreibendem Zugriff
        open_writeable();
        //Task-Objekt in die Datenbank schreiben und ID zurückbekommen
        long insertId = database.insert(DBHelper.TABLE_TODO_LIST, null, values);

        //Zeiger auf das gerade eingefügte TaskObjekt
        /*Cursor cursor = database.query(DBHelper.TABLE_TODO_LIST, columns,
                DBHelper.COLUMN_ID + " = ?",
                new String[]{String.valueOf(insertId)}, null, null, null);*/
        Cursor cursor = database.query(DBHelper.TABLE_TODO_LIST, columns,
                DBHelper.COLUMN_ID + " = " + insertId,
                null, null, null, null);

        //Zeiger auf den Anfang des Objekts setzen
        cursor.moveToFirst();
        //Aktuelles Objekt auslesen
        ToDo currentTask = cursorToTask(cursor);
        //Zeiger zerstören
        cursor.close();

        //Schließen der Datenank
        close_db();

        //Taskobjekt zurückgeben
        return currentTask;
    }

    //Löschen eines Objekts
    public void deleteTask(ToDo task) {
        open_writeable();
        database.delete(DBHelper.TABLE_TODO_LIST,
                DBHelper.COLUMN_ID + " = " + task.getId(),
                null);
        close_db();
    }

    //Update eines Objekts
    public void updateTask(ToDo task) {
        //String status = String.valueOf(task.isDone()) == "true" ? "1" : "0";
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_TASKCONTENT, task.getTodo());
        values.put(DBHelper.COLUMN_TASKSTATUS, task.isDone());

        open_writeable();
        database.update(DBHelper.TABLE_TODO_LIST, values, DBHelper.COLUMN_ID + " = " + task.getId(),
                null);
        close_db();
    }

    //Löschen aller Objekte
    public void deleteAllTasks(){
        open_writeable();
        //Query zum löschen aller Einträge
        String query = "DELETE FROM " + DBHelper.TABLE_TODO_LIST;
        //Löschen ausführen
        database.execSQL(query);

        //database.delete(DBHelper.TABLE_TODO_LIST, null, null);
        close_db();
    }

    //Hole alle Einträge aus der Datenbank
    public List<ToDo> getAllTasks(){
        List<ToDo> list = new ArrayList<>();
        String query = "SELECT * FROM " + DBHelper.TABLE_TODO_LIST;
        open_readable();

        //Zeiger auf Einträge in der Tabelle
        Cursor cursor = database.rawQuery(query, null);
        //Wenn der Zeiger beim ersten Eintrag ist
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToTask(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;

    }

    public List<ToDo> getAllUndone(){
        List<ToDo> list = new ArrayList<>();
        open_readable();

        //Zeiger auf Einträge in der Tabelle
        Cursor cursor = database.query(DBHelper.TABLE_TODO_LIST, columns,
                DBHelper.COLUMN_TASKSTATUS + " = 0" ,
                null, null, null, null);

        //Wenn der Zeiger beim ersten Eintrag ist
        if(cursor.moveToFirst()) {
            do {
                list.add(cursorToTask(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        close_db();
        return list;

    }


}
