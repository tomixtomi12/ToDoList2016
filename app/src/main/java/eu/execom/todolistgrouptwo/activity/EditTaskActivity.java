package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.model.Task;

@EActivity(R.layout.activity_edit_task)
public class EditTaskActivity extends AppCompatActivity {

    @ViewById
    TextInputEditText title;

    @ViewById
    TextInputEditText description;

    @ViewById
    ToggleButton isActive;

    private Task task;


    @AfterViews
    void init(){
        final String taskString = getIntent().getStringExtra("task");
        final Gson gson = new Gson();
        task = gson.fromJson(taskString, Task.class);

        title.setText(task.getTitle());
        description.setText(task.getDescription());
        isActive.setChecked(task.isFinished());
    }

    @Click
    void saveTask() {
        final Intent intent = new Intent();
        final Gson gson = new Gson();

        updateTask();

        intent.putExtra("task", gson.toJson(task));
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateTask() {
        task.setTitle(title.getText().toString());
        task.setDescription(description.getText().toString());
        task.setFinished(isActive.isChecked());
    }
}
