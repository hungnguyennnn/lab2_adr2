package com.example.lab2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoviewHolder> {

    List<Todo> todoList;

    static OnItemClickListener listener;

    interface OnItemClickListener {
        void onDeleteClick(int position);
        void onEditClick(int position);
        void onStatusChange(int position, boolean isDone);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        TodoAdapter.listener = listener;
    }

    public TodoAdapter(List<Todo> todoList) {
        this.todoList = todoList;
    }

    @NonNull
    @Override
    public TodoviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_view, parent, false);
        return new TodoviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoviewHolder holder, int position) {
        Todo currentTodo = todoList.get(position);
        holder.tvToDoName.setText(currentTodo.getTitle());
        holder.checkBox.setChecked(currentTodo.getStatus() == 1);

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onStatusChange(holder.getAdapterPosition(), isChecked);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public static class TodoviewHolder extends RecyclerView.ViewHolder {
        TextView tvToDoName;
        CheckBox checkBox;
        Button btnEdit, btnDelete;

        public TodoviewHolder(@NonNull View itemView) {
            super(itemView);

            tvToDoName = itemView.findViewById(R.id.item_tvToDoName);
            btnEdit = itemView.findViewById(R.id.item_btnEdit);
            btnDelete = itemView.findViewById(R.id.item_btnDelete);
            checkBox = itemView.findViewById(R.id.cbo_item);

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(pos);
                        }
                    }
                }
            });

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onEditClick(pos);
                        }
                    }
                }
            });
        }
    }
}
