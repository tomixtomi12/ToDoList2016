package eu.execom.todolistgrouptwo.database.dao;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.annotations.RestService;

import eu.execom.todolistgrouptwo.api.RestApi;
import eu.execom.todolistgrouptwo.model.User;
import eu.execom.todolistgrouptwo.model.dto.UserRegistrationDTO;

@EBean
public class UserDAO {

    @RestService
    RestApi restApi;

    public boolean create(User user) {
        try {
            restApi.createUser(new UserRegistrationDTO(user));
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
