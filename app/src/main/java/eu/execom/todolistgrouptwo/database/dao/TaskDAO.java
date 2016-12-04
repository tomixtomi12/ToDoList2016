package eu.execom.todolistgrouptwo.database.dao;


import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.model.Task;

@EBean
public class TaskDAO {

    @RestService
    RestApi restApi;

    public Task create(Task task) {
        return restApi.createTask(task);
    }

    public void updateTask(Task task) {
        Task responseTask = restApi.updateTask(task, task.getId());
        Log.i("Task response", responseTask.toString());
    }

}
