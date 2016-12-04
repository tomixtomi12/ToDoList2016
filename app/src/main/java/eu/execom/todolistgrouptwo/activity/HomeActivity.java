package eu.execom.todolistgrouptwo.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.KeyDown;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.web.client.RestClientException;

import java.util.List;

import eu.execom.todolistgrouptwo.R;
import eu.execom.todolistgrouptwo.adapter.TaskAdapter;
import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.database.dao.TaskDAO;
import eu.execom.todolistgrouptwo.database.dao.UserDAO;
import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.preference.UserPreferences_;

/**
 * Home {@link AppCompatActivity Activity} for navigation and listing all tasks.
 */
@EActivity(R.layout.activity_home)
public class HomeActivity extends AppCompatActivity {

    /**
     * Used for logging purposes.
     */
    private static final String TAG = HomeActivity.class.getSimpleName();

    /**
     * Used for identifying results from different activities.
     */
    protected static final int ADD_TASK_REQUEST_CODE = 42;
    protected static final int LOGIN_REQUEST_CODE = 420; // BLAZE IT
    protected static final int EDIT_TASK_REQUEST_CODE = 4200;

    /**
     * Tasks are kept in this list during a user session.
     */
    private List<Task> tasks;

    /**
     * {@link FloatingActionButton FloatingActionButton} for starting the
     * {@link AddTaskActivity AddTaskActivity}.
     */
    @ViewById
    FloatingActionButton addTask;

    /**
     * {@link ListView ListView} for displaying the tasks.
     */
    @ViewById
    ListView listView;

    /**
     * {@link TaskAdapter Adapter} for providing data to the {@link ListView listView}.
     */
    @Bean
    TaskAdapter adapter;

    @Bean
    UserDAO userDAO;

    @Bean
    TaskDAO taskDAO;

    @Pref
    UserPreferences_ userPreferences;

    @RestService
    RestApi restApi;


    @AfterViews
    @Background
    void checkUser() {
        if (!userPreferences.accessToken().exists()) {
            LoginActivity_.intent(this).startForResult(LOGIN_REQUEST_CODE);
            return;
        }

        try {
            tasks = restApi.getAllTasks();
        } catch (RestClientException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        initData();
    }


    private void startEditTask(Task task) {
        Intent intent = new Intent(this, EditTaskActivity_.class);
        final Gson gson = new Gson();
        intent.putExtra("task", gson.toJson(task));
        startActivityForResult(intent, EDIT_TASK_REQUEST_CODE);
    }


    /**
     * Loads tasks from the {@link android.content.SharedPreferences SharedPreferences}
     * and sets the adapter.
     */
    @UiThread
    void initData() {
        listView.setAdapter(adapter);
        adapter.setTasks(tasks);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    // button show
                    addTask.show();
                } else {
                    // button hide
                    addTask.hide();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        listView.setOnItemClickListener(new AbsListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Task task = adapter.getItem(position);
                startEditTask(task);
            }
        });
    }


    /**
     * Called when the {@link FloatingActionButton FloatingActionButton} is clicked.
     */
    @Click
    void addTask() {
        Log.i(TAG, "Add task clicked!");
        AddTaskActivity_.intent(this).startForResult(ADD_TASK_REQUEST_CODE);
    }


    /**
     * Called when the {@link AddTaskActivity AddTaskActivity} finishes.
     *
     * @param resultCode Indicates whether the activity was successful.
     * @param task         The new task.
     */
    @OnActivityResult(ADD_TASK_REQUEST_CODE)
    @Background
    void onResult(int resultCode, @OnActivityResult.Extra String task) {
        if (resultCode == RESULT_OK) {
//            Toast.makeText(this, task, Toast.LENGTH_SHORT).show();
            final Gson gson = new Gson();
            final Task newTask = gson.fromJson(task, Task.class);

            try {
                final Task newNewTask = taskDAO.create(newTask);
                onTaskCreated(newNewTask);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
        }
    }


    @OnActivityResult(EDIT_TASK_REQUEST_CODE)
    @Background
    void onTaskUpdated(int resultCode, @OnActivityResult.Extra("task") String task) {
        if (resultCode == RESULT_OK){
            final Gson gson = new Gson();
            final Task editedTask = gson.fromJson(task, Task.class);
            updateLocalTask(editedTask);
            updateDatabase(editedTask);
            updateViews();
        }
    }

    @UiThread
    void updateViews() {
        listView.invalidateViews();
    }

    private void updateDatabase(Task editedTask) {
        taskDAO.updateTask(editedTask);
    }

    private void updateLocalTask(Task editedTask) {
        for (Task t : adapter.getItems()) {
            if (t.getId() == editedTask.getId()) {
                t.setTitle(editedTask.getTitle());
                t.setDescription(editedTask.getDescription());
                t.setFinished(editedTask.isFinished());
                break;
            }
        }
    }


    @UiThread
    void onTaskCreated(Task task) {
        tasks.add(task);
        adapter.addTask(task);
    }


    @OnActivityResult(LOGIN_REQUEST_CODE)
    void onLogin(int resultCode, @OnActivityResult.Extra("token") String token) {
        if (resultCode == RESULT_OK) {
            userPreferences.accessToken().put(token);
            checkUser();
        }
    }


    @KeyDown(KeyEvent.KEYCODE_BACK)
    void logout() {
        userPreferences.clear();
        checkUser();
    }
}
