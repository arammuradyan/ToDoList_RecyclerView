package com.example.todolist10;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public RecyclerView recyclerView;
    public TaskAdapter taskAdapter;
    private final int ADD_REQUEST_CODE = 12;
    private final int DELETE_SAVE_REQUEST_CODE = 23;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        recyclerView = findViewById(R.id.rv);
        taskAdapter = new TaskAdapter(new TaskAdapter.Listener() {

            @Override
            public void onTaskItemClick(int i) {

                Intent intent = new Intent(MainActivity.this, PreviewActivity.class);
                Bundle taskBundle = new Bundle();
                taskBundle.putParcelable("TaskToPreview", taskAdapter.getTaskList().get(i));
                intent.putExtra("TaskToPreview", taskBundle);
                intent.putExtra("index", i);
                startActivityForResult(intent,DELETE_SAVE_REQUEST_CODE );
            }
        });


        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setAdapter(taskAdapter);
        recyclerView.setLayoutManager(llm);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent addIntent = new Intent(MainActivity.this, PreviewActivity.class);
                startActivityForResult(addIntent, ADD_REQUEST_CODE);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_REQUEST_CODE && data != null) {
            Bundle addBundle = data.getBundleExtra("addTask");
            Task addTask = (Task) addBundle.get("addTask");
            taskAdapter.add(addTask);
        }

            if (requestCode == DELETE_SAVE_REQUEST_CODE && data != null) {
                int index = data.getIntExtra("index", -10);
                if (data.getBundleExtra("saveTask")!=null) {
                    Bundle saveBundle = data.getBundleExtra("saveTask");
                    Task saveTask = (Task) saveBundle.get("saveTask");
                    taskAdapter.save(index, saveTask);
                } else {

                     taskAdapter.delete(index);
                    }
                }
    }


        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
