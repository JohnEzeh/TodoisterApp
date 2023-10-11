package com.bawp.todoister;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.adapter.OnTodoClickListener;
import com.bawp.todoister.adapter.RecyclerViewAdapter;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements OnTodoClickListener {
    TaskViewModel taskViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private int counter;
    BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        counter = 0;
         bottomSheetFragment = new BottomSheetFragment();

         ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
         BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
         bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

       taskViewModel = new ViewModelProvider.AndroidViewModelFactory(
               MainActivity.this.getApplication()).create(TaskViewModel.class);

       sharedViewModel = new ViewModelProvider(this)
               .get(SharedViewModel.class);

       //TaskViewModel instantiation
            taskViewModel.getAllTasks().observe(this, tasks -> {

                 recyclerViewAdapter = new RecyclerViewAdapter(tasks,this);
                recyclerView.setAdapter(recyclerViewAdapter);

            });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
//            Task task = new Task("Task" + counter++, Priority.MEDIUM, Calendar.getInstance().getTime(),
//            Calendar.getInstance().getTime(), false);
//            TaskViewModel.insert(task);

            showBottomSheetDialog();
        });
    }
       //bottomSheetDialog method
    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(),
                bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
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

    @Override
    public void onTodoClick( Task task) {

        sharedViewModel.selectItem(task);
            sharedViewModel.setIsEdit(true);
        showBottomSheetDialog();
//        Log.d("MAIN", "onTodoClick: " + task.getTask());
    }

    @Override
    public void onTodoRadioButton(Task task) {
        Log.d("MAIN", "onRadio  Click: " + task.getTask());

       TaskViewModel.delete(task);
       recyclerViewAdapter.notifyDataSetChanged();
    }


}