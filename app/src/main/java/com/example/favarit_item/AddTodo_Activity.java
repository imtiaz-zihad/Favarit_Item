package com.example.favarit_item;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AddTodo_Activity extends AppCompatActivity {

    DataBaseHalper dataBaseHalper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_todo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        dataBaseHalper = new DataBaseHalper(this);
        EditText edText = findViewById(R.id.edText);
        EditText edDate = findViewById(R.id.edDate);
        FloatingActionButton addTodoButton = findViewById(R.id.addTodoButton);


        addTodoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtodo = edText.getText().toString();
                String eddate = edDate.getText().toString();
                dataBaseHalper.InsertData(edtodo);
                Toast.makeText(AddTodo_Activity.this, "todo added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddTodo_Activity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });








    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}