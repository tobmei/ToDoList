package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToDoArrayAdapter extends ArrayAdapter<ToDo> {

    private Context context;
    private List<ToDo> itemList;
    LayoutInflater layoutInflater; //verbindet Layout-XML-Datei mit unserem JavaCode

    public ToDoArrayAdapter(@NonNull Context context, List<ToDo> itemList) {
        super(context, R.layout.list_row, itemList);

        this.context = context;
        this.itemList = itemList;
        this.layoutInflater = layoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        //erzeugen der View Hierarchie auf Grundlage des Zeilenlayouts
        View rowView;
        if(convertView == null)
            rowView = layoutInflater.inflate(R.layout.list_row, parent, false);
        else
            rowView = convertView;

        //Anfordern des zur Listenposition gehörenden Items
        ToDo item = itemList.get(position);

        //Finden der einzelnen View Objekte der View Hierarchie
        TextView txtvToDo = (TextView) rowView.findViewById(R.id.txtvToDo);
        ImageView ivBox = (ImageView) rowView.findViewById(R.id.ivBox);
        TextView txtvTime= (TextView) rowView.findViewById(R.id.txtvDate);

        //Felder befüllen
        txtvToDo.setText(item.getTodo());
        txtvTime.setText(item.getTime());
        if(item.isDone())
            ivBox.setImageResource(android.R.drawable.checkbox_on_background);
        else
            ivBox.setImageResource(android.R.drawable.checkbox_off_background);

        return rowView;
    }
}
