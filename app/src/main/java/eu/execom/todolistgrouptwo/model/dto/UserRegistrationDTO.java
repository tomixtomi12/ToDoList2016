package eu.execom.todolistgrouptwo.model.dto;

import eu.execom.todolistgrouptwo.model.User;

/**
 * Created by Tamas on 12/4/2016.
 */

public class UserRegistrationDTO {
    private String email;

    private String password;

    private String confirmPassword;

    public UserRegistrationDTO(User user) {
        email = user.getUsername();
        password = user.getPassword();
        confirmPassword = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
