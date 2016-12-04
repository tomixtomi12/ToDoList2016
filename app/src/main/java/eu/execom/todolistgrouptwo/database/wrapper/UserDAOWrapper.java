package eu.execom.todolistgrouptwo.database.wrapper;

import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;
import org.androidannotations.rest.spring.annotations.RestService;

import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.database.DatabaseHelper;
import eu.execom.todolistgrouptwo.database.dao.UserDAO;
import eu.execom.todolistgrouptwo.model.User;
import eu.execom.todolistgrouptwo.model.dto.UserRegistrationDTO;

@EBean
public class UserDAOWrapper {

    @OrmLiteDao(helper = DatabaseHelper.class)
    UserDAO userDAO;

    @RestService
    RestApi restApi;

    public User findByUsernameAndPassword(String username, String password) {
        return userDAO.findByUsernameAndPassword(username, password);
    }

    public boolean create(User user) {
        try {
            restApi.createUser(new UserRegistrationDTO(user));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public User findById(long userId) {
        return userDAO.queryForId(userId);
    }
}
