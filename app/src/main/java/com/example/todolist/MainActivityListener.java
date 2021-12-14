package com.example.todolist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.todolist.MainActivity;
import com.example.todolist.ToDo;
import com.example.todolist.ToDoArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivityListener implements AdapterView.OnItemClickListener {

    MainActivity mainActivity;
    ToDoArrayAdapter todoArrayAdapter;
    List<ToDo> todoList = new ArrayList<>();
    DataSource dataSource;

    public MainActivityListener(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        dataSource = new DataSource(mainActivity);
        initListView();
    }

    private ArrayList<ToDo> initToDo(int anzahl){
        ArrayList<ToDo> td = new ArrayList<>();
        for(int i=0;i<anzahl;i++){
            ToDo todo = new ToDo("ToDo " + i, false);
            td.add(dataSource.insertTask(todo));
        }
        return td;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.om_addEntry:
                addDialog();
                todoArrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.omSettings:
                Intent settings = new Intent(mainActivity, SettingsActivity.class);
                mainActivity.startActivity(settings);
                return true;
            case R.id.omDeleteAll:
                todoList.clear();
                dataSource.deleteAllTasks();
                todoArrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.omCreateDummy:
                todoList.addAll(initToDo(5));
                todoArrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.omRefresh:
                initListView();
                return true;
        }
        return false;
    }

    public boolean onContextItemSeleceted(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ToDo selectedToDo = todoList.get(info.position);
        switch(item.getItemId()){
            case R.id.cm_deleteEntry:
                todoList.remove(selectedToDo);
                todoArrayAdapter.notifyDataSetChanged();
                dataSource.deleteTask(selectedToDo);
                return true;
            case R.id.cm_editEntry:
                editDialog(selectedToDo);
                return true;
        }
        return false;
    }

    private void addDialog(){
        //Ein Eingabefeld definieren und instanziieren
        final EditText txtEditTitle = new EditText(mainActivity);

        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle("Add ToDo")
                .setMessage("Add a ToDo to your list")
                .setView(txtEditTitle)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ToDo todo = new ToDo(txtEditTitle.getText().toString(), false);
                        todoList.add(0,dataSource.insertTask(todo));
                        todoArrayAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton(android.R.string.no, null);

        alert.show();
    }

    private void editDialog(final ToDo selectedToDo){
        //Ein Eingabefeld definieren und instanziieren
        final EditText txtEditTitle = new EditText(mainActivity);
        //ZitatTitel an EditText übergeben
        txtEditTitle.setText(selectedToDo.getTodo());

        AlertDialog.Builder alert = new AlertDialog.Builder(mainActivity);
        alert.setTitle("Edit ToDo")
                .setMessage("Edit your ToDo")
                .setView(txtEditTitle)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedToDo.setTodo(txtEditTitle.getText().toString());
                        todoArrayAdapter.notifyDataSetChanged();
                        dataSource.updateTask(selectedToDo);
                    }
                })
                .setNegativeButton(android.R.string.no, null);
        alert.show();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ToDo currentToDo = (ToDo) adapterView.getItemAtPosition(i);
        currentToDo.setDone(!currentToDo.isDone());
        dataSource.updateTask(currentToDo);
        todoArrayAdapter.notifyDataSetChanged();
    }

    private void initListView(){
        SharedPreferences sp = mainActivity.getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean onlyUndone = false;
        if(sp.contains("Check")){
            onlyUndone = sp.getBoolean("Check", false);
        }
        if(onlyUndone)
            todoList = dataSource.getAllUndone();
        else
            todoList = dataSource.getAllTasks();

        //todoList = dataSource.getAllTasks();
        todoArrayAdapter = new ToDoArrayAdapter(mainActivity, todoList);
        mainActivity.lvToDo.setAdapter(todoArrayAdapter);
    }
}
