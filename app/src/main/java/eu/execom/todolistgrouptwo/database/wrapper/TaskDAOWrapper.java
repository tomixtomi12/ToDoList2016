package eu.execom.todolistgrouptwo.database.wrapper;


import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.List;

import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.database.DatabaseHelper;
import eu.execom.todolistgrouptwo.database.dao.TaskDAO;
import eu.execom.todolistgrouptwo.model.Task;
import eu.execom.todolistgrouptwo.model.User;

@EBean
public class TaskDAOWrapper {

    @OrmLiteDao(helper = DatabaseHelper.class)
    TaskDAO taskDAO;

    @RestService
    RestApi restApi;

    public List<Task> findByUser(User user) {
        return taskDAO.findByUser(user);
    }

    public Task create(Task task) {
        return restApi.createTask(task);
    }

    public void updateTask(Task task) {
        Task responseTask = restApi.updateTask(task, task.getId());
        Log.i("Task response", responseTask.toString());
    }

}
