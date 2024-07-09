package com.example.lab2;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText edtTitle_lab2, edtConTent_lab2,edtDate_lab2,edtType_lab2;
    Button btnAdd_lab2;
    RecyclerView RecyclerView_lab2;
    TodoAdapter adapter;
    TodoDAO todoDAO;
    List<Todo> todoList;
    Todo currenEditingTodo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        edtTitle_lab2 = findViewById(R.id.edtTitle_lab2);
        edtConTent_lab2 = findViewById(R.id.edtConTent_lab2);
        edtDate_lab2 = findViewById(R.id.edtDate_lab2);
        edtType_lab2 = findViewById(R.id.edtType_lab2);
        btnAdd_lab2 = findViewById(R.id.btnAdd_lab2);
        RecyclerView_lab2 = findViewById(R.id.RecyclerView_lab2);

        todoDAO = new TodoDAO(this);
        todoList = todoDAO.getAllTodos();
        adapter= new TodoAdapter(todoList);
        RecyclerView_lab2.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        RecyclerView_lab2.setAdapter(adapter);
        btnAdd_lab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currenEditingTodo== null){
                    addTodo();
                }else {
                    updateTodo();
                }
            }
        });


        adapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onDeleteClick(int position) {
                deleteTodo(position);
            }

            @Override
            public void onEditClick(int position) {
                editTodo(position);
            }

            @Override
            public void onStatusChange(int position, boolean isDone) {
                updateTodoStatus(position,isDone);
            }
        });
    }
    private void updateTodoStatus(int pos, boolean isDone){
        Todo todo = todoList.get(pos);
        todo.setStatus(isDone?1:0);
        todoDAO.updateTodoStatus(todo.getId(),todo.getStatus());
//        adapter.notifyItemChanged(pos);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                adapter.notifyItemChanged(pos);
            }
        });
        Toast.makeText(this, "Update thành công", Toast.LENGTH_SHORT).show();
    }


    void addTodo(){
        String title = edtTitle_lab2.getText().toString();
        String content = edtConTent_lab2.getText().toString();
        String date = edtDate_lab2.getText().toString();
        String type = edtType_lab2.getText().toString();
        Todo todo= new Todo(0,title,content,date,type,0);
        todoDAO.addTodo(todo);
        todoList.add(0,todo);
        adapter.notifyItemInserted(0);
        RecyclerView_lab2.scrollToPosition(0);
        clearFields();
    }

    void updateTodo(){
        String title = edtTitle_lab2.getText().toString();
        String content = edtConTent_lab2.getText().toString();
        String date = edtDate_lab2.getText().toString();
        String type = edtType_lab2.getText().toString();
        currenEditingTodo.setTitle(title);
        currenEditingTodo.setContent(content);
        currenEditingTodo.setDate(date);
        currenEditingTodo.setType(type);
        todoDAO.updateTodo(currenEditingTodo);
        int pos = todoList.indexOf(currenEditingTodo);
        adapter.notifyItemChanged(pos);
        currenEditingTodo=null;
        btnAdd_lab2.setText("Add");
        clearFields();
    }

    void editTodo(int pos){
        currenEditingTodo= todoList.get(pos);
        edtTitle_lab2.setText(currenEditingTodo.getTitle());
        edtConTent_lab2.setText(currenEditingTodo.getContent());
        edtDate_lab2.setText(currenEditingTodo.getDate());
        edtType_lab2.setText(currenEditingTodo.getType());
        btnAdd_lab2.setText("Update");
    }

    void deleteTodo(int pos){
        Todo todo = todoList.get(pos);
        todoDAO.daleteTodo(todo.getId());
        todoList.remove(pos);
        adapter.notifyItemRemoved(pos);
    }
     private void clearFields(){
        edtTitle_lab2.setText("");
        edtConTent_lab2.setText("");
        edtDate_lab2.setText("");
        edtType_lab2.setText("");
     }
}