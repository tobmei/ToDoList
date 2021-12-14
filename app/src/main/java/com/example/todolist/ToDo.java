package com.example.todolist;

public class ToDo {

    private int id;
    private String todo;
    private boolean isDone;
    private String time;

    public ToDo(String todo, boolean isDone) {
        this.todo = todo;
        this.isDone = isDone;
        time = java.time.LocalDate.now().toString();
    }

    public ToDo(int id, String todo, boolean isDone, String time) {
        this.id = id;
        this.todo = todo;
        this.isDone = isDone;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime(){
        return time;
    }

    public String getTodo() {
        return todo;
    }

    public void setTodo(String todo) {
        this.todo = todo;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }
}
